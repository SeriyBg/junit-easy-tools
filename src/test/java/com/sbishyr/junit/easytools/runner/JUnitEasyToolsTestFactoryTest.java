package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.TestFactory;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static com.sbishyr.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsTestFactoryTest {

    @RunWith(JUnitEasyTools.class)
    public static class ClassWithTestFactory {

        private final String injectedString;

        public ClassWithTestFactory(String injectedString) {
            this.injectedString = injectedString;
        }

        @Test
        public void a() {
            assertThat(injectedString).isEqualTo("receivedFromFactory");
        }

        @TestFactory
        public static ClassWithTestFactory testClass() {
            return new ClassWithTestFactory("receivedFromFactory");
        };
    }

    @Test
    public void shouldInitAndRunTestCreatedWithFactory() throws Exception {
        Result result = JUnitCore.runClasses(ClassWithTestFactory.class);
        assertResultHasNoFailures(result);
    }
}
