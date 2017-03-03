package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.DataProducer;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Serge Bishyr
 */
public class JUnitDataProducerTest {

    @RunWith(JUnitDataProducer.class)
    public static class JunitTestClass {
        @Test public void a(){}
    }

    @RunWith(JUnitDataProducer.class)
    public static class TestWithProducedData {
        @DataProducer
        public static IntSupplier intProducer = () -> 1;

        @DataProducer
        public static Supplier<String> stringProducer = () -> "test-string";

        @Test public void a(int i){
            assertThat(i).isEqualTo(1);
        }

        @Test public void b(String string) {
            assertThat(string).isEqualTo("test-string");
        }
    }

    @Test
    public void shouldRunJunitTestMethod() throws Exception {
        Result result = JUnitCore.runClasses(JunitTestClass.class);
        assertThat(result.getRunCount()).isEqualTo(1);
        assertThat(result.wasSuccessful()).isTrue();
    }

    @Test
    public void shouldRunWithInsertedProducedData() throws Exception {
        Result result = JUnitCore.runClasses(TestWithProducedData.class);
        assertThat(result.getRunCount()).isEqualTo(2);
        assertThat(result.wasSuccessful()).isTrue();
    }
}
