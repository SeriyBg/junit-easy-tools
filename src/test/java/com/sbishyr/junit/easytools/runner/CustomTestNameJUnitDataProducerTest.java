package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.function.Supplier;

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

        @Test
        public void b() {
            assertThat(testName.getMethodName()).isEqualTo("b");
        }
    }

    @RunWith(JUnitDataProducer.class)
    public static class CustomNameWithParameters {

        @Rule
        public TestName testName = new TestName();

        @DataProducer
        public static Supplier<String> stringSupplier = () -> "supplied custom name";

        @Test
        @ProducedValues(name = "{0}")
        public void a(String s) {
            assertThat(testName.getMethodName()).isEqualTo("a[" + s + "]");
        }
    }

    @Test
    public void shouldProvideCustomTestName() throws Exception {
        Result result = JUnitCore.runClasses(CustomDisplayNameTestClass.class);
        assertResultHasNoFailures(result);
    }

    @Test
    public void shouldIncludeParametersInTestName() throws Exception {
        Result result = JUnitCore.runClasses(CustomNameWithParameters.class);
        assertResultHasNoFailures(result);
    }
}
