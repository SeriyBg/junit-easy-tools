package com.sbishyr.junit.easytools.runner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsValidationTest {

    @RunWith(JUnitEasyTools.class)
    public static class NoDataProducer {

        @Test(expected = InitializationError.class)
        public void a(String s) {
            //do nothing
        }
    }

    @Test
    public void shouldThrowExceptionOnNoDataProducer() throws Exception {
        Result result = JUnitCore.runClasses(NoDataProducer.class);
        assertThat(result.getFailureCount()).isEqualTo(1);
    }
}