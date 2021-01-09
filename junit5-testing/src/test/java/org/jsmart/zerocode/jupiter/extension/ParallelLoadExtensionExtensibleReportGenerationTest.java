package org.jsmart.zerocode.jupiter.extension;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.domain.TestMappings;
import org.jsmart.zerocode.tests.jupiter.JUnit5Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelLoadExtensionExtensibleReportGenerationTest {

    @ExtendWith({ParallelLoadExtensionExtensibleReportGeneration.class})
    public class JUnit5ExampleLoad {

        @Test
        //@LoadWith("load_generation.properties") //missing case
        @TestMappings({
                @TestMapping(testClass = JUnit5Test.class, testMethod = "testX"),
                @TestMapping(testClass = JUnit5Test.class, testMethod = "testY")
        })
        public void testLoad() {
            /* No code needed here */
        }
    }

    @Test
    void testValidation_exception() {
        Class<ParallelLoadExtensionExtensibleReportGenerationTest.JUnit5ExampleLoad> clazz = ParallelLoadExtensionExtensibleReportGenerationTest.JUnit5ExampleLoad.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method testMethod = declaredMethods[0]; // this is 'testLoad()'

        ParallelLoadExtensionExtensibleReportGeneration parallelLoadExtension = new ParallelLoadExtensionExtensibleReportGeneration();
        RuntimeException thrown =
                assertThrows(RuntimeException.class,
                        () -> parallelLoadExtension.validateAndGetLoadPropertiesFile(clazz, testMethod),
                        "Expected to throw RuntimeException, but it didn't throw.");

        assertTrue(thrown.getMessage().contains("Missing the the @LoadWith(...)"));
    }

    @ExtendWith({ParallelLoadExtensionExtensibleReportGeneration.class})
    public class JUnit5ExampleLoad2 {

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

    @Test
    void testLoadAnnotation_success() {
        Class<ParallelLoadExtensionExtensibleReportGenerationTest.JUnit5ExampleLoad2> clazz = ParallelLoadExtensionExtensibleReportGenerationTest.JUnit5ExampleLoad2.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method testMethod = declaredMethods[0]; // this is 'testLoad()'

        ParallelLoadExtensionExtensibleReportGeneration parallelLoadExtension = new ParallelLoadExtensionExtensibleReportGeneration();
        String loadPropertiesFile = parallelLoadExtension.validateAndGetLoadPropertiesFile(clazz, testMethod);

        assertEquals("load_generation.properties", loadPropertiesFile);
    }
}
