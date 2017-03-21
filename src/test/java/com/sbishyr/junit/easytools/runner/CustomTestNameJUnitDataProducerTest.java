package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
@RunWith(JUnitDataProducer.class)
public class CustomTestNameJUnitDataProducerTest {

    @Rule
    public TestName testName = new TestName();

    @DataProducer
    public static Supplier<String> stringSupplier = () -> "supplied custom name";

    @DataProducer
    public static IntSupplier intSupplier = () -> 42;

    @Test
    public void shouldHaveOriginalName() throws Exception {
        assertThat(testName.getMethodName()).isEqualTo("shouldHaveOriginalName");
    }

    @Test
    @ProducedValues(name = "custom test name")
    public void shouldProvideCustomTestName() throws Exception {
        assertThat(testName.getMethodName()).isEqualTo("shouldProvideCustomTestName[custom test name]");
    }

    @Test
    @ProducedValues(name = "{0}")
    public void shouldIncludeParameterToTestName(String s) throws Exception {
        assertThat(testName.getMethodName()).isEqualTo("shouldIncludeParameterToTestName[supplied custom name]");
    }

    @Test
    @ProducedValues(name = "{0}, {1}")
    public void shouldIncludeParametersOfDifferentType(String s, int i) throws Exception {
        assertThat(testName.getMethodName())
                .isEqualTo("shouldIncludeParametersOfDifferentType[supplied custom name, 42]");

    }

    @Test
    @ProducedValues(name = "{index}")
    public void shouldIncludeIndex() throws Exception {
        assertThat(testName.getMethodName()).isEqualTo("shouldIncludeIndex[0]");
    }
}
