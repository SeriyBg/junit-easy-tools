package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import com.sbishyr.junit.easytools.utils.ResultAssertions;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.function.Function;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsFunctionDataProducerTest {

    @RunWith(JUnitEasyTools.class)
    public static class SimpleFunction {

        @DataProducer
        public static Function<String, String> dataProducer = String::toUpperCase;

        @Test
        @ProducedValues(withValues = {"produced"})
        public void a(String s) {
            Assertions.assertThat(s).isEqualTo("PRODUCED");
        }
    }

    @Test
    public void shouldAcceptFunctionAsDataProducer() throws Exception {
        Result result = JUnitCore.runClasses(SimpleFunction.class);
        ResultAssertions.assertResultHasNoFailures(result);
    }
}