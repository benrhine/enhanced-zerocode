package org.jsmart.zerocode.core.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsmart.zerocode.core.domain.builders.ZeroCodeCsvReportBuilder;
import org.jsmart.zerocode.core.domain.reports.ZeroCodeExecResult;
import org.jsmart.zerocode.core.domain.reports.csv.ZeroCodeCsvReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZeroCodeReportGeneratorExtendedImpl extends ZeroCodeReportGeneratorImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCodeReportGeneratorExtendedImpl.class);

    private final Boolean groupResultByTest;

    private final Boolean timestampTimeOnly;

    public ZeroCodeReportGeneratorExtendedImpl(ObjectMapper mapper) {
        this(mapper, false, false);
    }

    public ZeroCodeReportGeneratorExtendedImpl(ObjectMapper mapper, Boolean groupResultByTest, Boolean timestampTimeOnly) {
        super(mapper);
        this.groupResultByTest = groupResultByTest;
        this.timestampTimeOnly = timestampTimeOnly;
    }

    public Map<String, List<ZeroCodeExecResult>> groupByTestCase() {
        Map<String, List<ZeroCodeExecResult>> map = new HashMap<>();

        this.treeReports.forEach((thisReport) -> {
            thisReport.getResults().forEach((thisResult) -> {
                thisResult.getSteps().forEach((thisStep) -> {
                    List<ZeroCodeExecResult> temp = new ArrayList<>();
                    final String key = thisStep.getName();
                    if (!map.containsKey(key)) {
                        temp.add(thisResult);
                        map.put(key, temp);
                    } else {
                        temp = map.get(key);
                        temp.add(thisResult);
                        map.put(key, temp);
                    }
                });
            });
        });

        return map;
    }

    public void ungroupedRows(final ZeroCodeCsvReportBuilder csvFileBuilder) {
        this.treeReports.forEach((thisReport) -> {
            thisReport.getResults().forEach((thisResult) -> {
                csvFileBuilder.scenarioLoop(thisResult.getLoop());
                csvFileBuilder.scenarioName(thisResult.getScenarioName());
                thisResult.getSteps().forEach((thisStep) -> {
                    csvFileBuilder.stepLoop(thisStep.getLoop());
                    csvFileBuilder.stepName(thisStep.getName());
                    csvFileBuilder.correlationId(thisStep.getCorrelationId());
                    csvFileBuilder.result(thisStep.getResult());
                    csvFileBuilder.method(thisStep.getOperation());

                    if (timestampTimeOnly) {
                        csvFileBuilder.requestTimeStamp(thisStep.getRequestTimeStamp().toLocalTime().toString());
                        csvFileBuilder.responseTimeStamp(thisStep.getResponseTimeStamp().toLocalTime().toString());
                    } else {
                        csvFileBuilder.requestTimeStamp(thisStep.getRequestTimeStamp().toString());
                        csvFileBuilder.responseTimeStamp(thisStep.getResponseTimeStamp().toString());
                    }
                    csvFileBuilder.responseDelayMilliSec(thisStep.getResponseDelay());
                    this.csvRows.add(csvFileBuilder.build());
                });
            });
        });
    }

    public void groupedRows(final ZeroCodeCsvReportBuilder csvFileBuilder) {
        final Map<String, List<ZeroCodeExecResult>> map = groupByTestCase();

        map.forEach((key, value) -> {
            value.forEach((thisResult) -> {
                csvFileBuilder.scenarioLoop(thisResult.getLoop());
                csvFileBuilder.scenarioName(thisResult.getScenarioName());
                thisResult.getSteps().forEach((thisStep) -> {
                    csvFileBuilder.stepLoop(thisStep.getLoop());
                    csvFileBuilder.stepName(thisStep.getName());
                    csvFileBuilder.correlationId(thisStep.getCorrelationId());
                    csvFileBuilder.result(thisStep.getResult());
                    csvFileBuilder.method(thisStep.getOperation());

                    if (timestampTimeOnly) {
                        csvFileBuilder.requestTimeStamp(thisStep.getRequestTimeStamp().toLocalTime().toString());
                        csvFileBuilder.responseTimeStamp(thisStep.getResponseTimeStamp().toLocalTime().toString());
                    } else {
                        csvFileBuilder.requestTimeStamp(thisStep.getRequestTimeStamp().toString());
                        csvFileBuilder.responseTimeStamp(thisStep.getResponseTimeStamp().toString());
                    }
                    csvFileBuilder.responseDelayMilliSec(thisStep.getResponseDelay());
                    this.csvRows.add(csvFileBuilder.build());
                });
            });
        });
    }

    @Override
    public List<ZeroCodeCsvReport> buildCsvRows() {
        /*
         * Map the java list to CsvPojo
         */
        final ZeroCodeCsvReportBuilder csvFileBuilder = ZeroCodeCsvReportBuilder.newInstance();

        if (groupResultByTest) {
            LOGGER.info("#### Grouped rows ####");
            this.groupedRows(csvFileBuilder);
        } else {
            LOGGER.info("#### Ungrouped rows ####");
            this.ungroupedRows(csvFileBuilder);
        }

        return csvRows;
    }
}
