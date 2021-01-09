package org.jsmart.zerocode.jupiter.extension;

import org.jsmart.zerocode.core.di.provider.ObjectMapperProvider;
import org.jsmart.zerocode.core.report.ZeroCodeReportGeneratorExtendedImpl;

public class ParallelLoadExtensionExtensibleReportGeneration extends ParallelLoadExtension {

    public ParallelLoadExtensionExtensibleReportGeneration() {
        super(new ZeroCodeReportGeneratorExtendedImpl(new ObjectMapperProvider().get(), true, true));
    }
}
