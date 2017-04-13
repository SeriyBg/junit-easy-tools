package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.sbishyr.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsDataProducerMethodTest {

    @RunWith(JUnitEasyTools.class)
    public static class MethodAsDataProducer {

        @DataProducer
        public static List<Supplier<String>> dataProducer() {
            return Arrays.asList(
                    () -> "return"
            );
        }

        @Test
        @Ignore("#29")
        public void a(String s) {
            assertThat(s).isEqualTo("return");
        }
    }

    @Test
    @Ignore("#29")
    public void shouldSupportMethodWithCollectionReturnAsDataProducer() throws Exception {
        Result result = JUnitCore.runClasses(MethodAsDataProducer.class);
        assertResultHasNoFailures(result);
    }
}