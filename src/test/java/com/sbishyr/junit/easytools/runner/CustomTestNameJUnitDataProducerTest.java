package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static com.sbishyr.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class CustomTestNameJUnitDataProducerTest {

    @RunWith(JUnitDataProducer.class)
    public static class CustomDisplayNameTestClass {

        @Rule
        public TestName testName = new TestName();

        @Test
        @ProducedValues(name = "custom test name")
        public void a() {
            assertThat(testName.getMethodName()).isEqualTo("a[custom test name]");
        }
    }

    @Test
    public void shouldProvideCustomTestName() throws Exception {
        Result result = JUnitCore.runClasses(CustomDisplayNameTestClass.class);
        assertResultHasNoFailures(result);
    }
}
