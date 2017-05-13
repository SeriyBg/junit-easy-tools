package com.github.seriybg.junit.easytools.runner;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValue;
import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.github.seriybg.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsDataProducerMethodTest {

    @RunWith(JUnitEasyTools.class)
    public static class MethodAsDataProducer {

        @DataProducer
        public static List<Supplier<String>> dataProducer() {
            return Collections.singletonList(
                    () -> "return"
            );
        }

        @Test
        public void a(String s) {
            assertThat(s).isEqualTo("return");
        }
    }

    @RunWith(JUnitEasyTools.class)
    public static class NamedMethodAsDataProducer {

        @DataProducer
        public static List<Supplier<String>> notUsed() {
            return Collections.singletonList(
                    () -> "42"
            );
        }

        @DataProducer
        public static List<Supplier<String>> dataProducer() {
            return Collections.singletonList(
                    () -> "value"
            );
        }

        @Test
        public void a(@ProducedValue(producer = "dataProducer") String s) {
            assertThat(s).isEqualTo("value");
        }
    }

    @Test
    public void shouldSupportMethodWithCollectionReturnAsDataProducer() throws Exception {
        Result result = JUnitCore.runClasses(MethodAsDataProducer.class);
        assertResultHasNoFailures(result);
    }

    @Test
    public void shouldSupportNamedDataProducers() throws Exception {
        Result result = JUnitCore.runClasses(NamedMethodAsDataProducer.class);
        assertResultHasNoFailures(result);
    }
}