package com.github.seriybg.junit.easytools.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsValidationTest {

    @RunWith(JUnitEasyTools.class)
    public static class NoDataProducer {

        @Test
        @Ignore("Method should throw InitializationError. Ignore to prevent running.")
        public void a(String s) {
            fail("Should not run!");
            //do nothing
        }
    }

    @Test
    public void shouldThrowExceptionOnNoDataProducer() throws Throwable {
        Method testMethod = NoDataProducer.class.getMethod("a", String.class);
        FrameworkMethod method = new FrameworkMethod(testMethod);
        FrameworkMethod spyMethod = spy(method);
        when(spyMethod.getAnnotation(Ignore.class)).thenReturn(null);
        assertThatExceptionOfType(InitializationError.class).isThrownBy(() ->
                new JUnitEasyTools(NoDataProducer.class).methodBlock(method).evaluate());
    }
}