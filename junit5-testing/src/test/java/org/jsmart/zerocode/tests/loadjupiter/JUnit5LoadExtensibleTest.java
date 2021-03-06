package org.jsmart.zerocode.tests.loadjupiter;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.domain.TestMappings;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtensionExtensibleReportGeneration;
import org.jsmart.zerocode.tests.jupiter.JUnit5Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ParallelLoadExtensionExtensibleReportGeneration.class})
public class JUnit5LoadExtensibleTest {

    @Test
    @LoadWith("load_generation.properties")
    @TestMappings({
            @TestMapping(testClass = JUnit5Test.class, testMethod = "testX"),
            @TestMapping(testClass = JUnit5Test.class, testMethod = "testY")
    })
    public void testLoad() {
        /* No code needed here */
    }
}
