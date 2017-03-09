package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.function.*;

import static com.sbishyr.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Serge Bishyr
 */
public class JUnitDataProducerTest {

    @RunWith(JUnitDataProducer.class)
    public static class JunitTestClass {
        @Test public void a(){
            //Do nothing. Check that method runs
        }
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

    @RunWith(JUnitDataProducer.class)
    public static class PrimitiveSuppliers {

        @DataProducer
        public static LongSupplier longSupplier = () -> 42l;

        @DataProducer
        public static BooleanSupplier booleanSupplier = () -> true;

        @DataProducer
        public static IntSupplier intProducer = () -> 27;

        @DataProducer
        public static DoubleSupplier doubleSupplier = () -> 4.2;

        @Test
        public void a(long l) {
            assertThat(l).isEqualTo(42l);
        }

        @Test
        public void b(boolean b) {
            assertThat(b).isTrue();
        }

        @Test
        public void c(int i) {
            assertThat(i).isEqualTo(27);
        }

        @Test
        public void d(double d) {
            assertThat(d).isEqualTo(4.2);
        }
    }

    @Test
    public void shouldRunJunitTestMethod() throws Exception {
        Result result = JUnitCore.runClasses(JunitTestClass.class);
        assertThat(result.getRunCount()).isEqualTo(1);
        assertResultHasNoFailures(result);
    }

    @Test
    public void shouldRunWithInsertedProducedData() throws Exception {
        Result result = JUnitCore.runClasses(TestWithProducedData.class);
        assertThat(result.getRunCount()).isEqualTo(2);
        assertResultHasNoFailures(result);
    }

    @Test
    public void shouldSupplyPrimitiveValues() throws Exception {
        Result result = JUnitCore.runClasses(PrimitiveSuppliers.class);
        assertThat(result.getRunCount()).isEqualTo(4);
        assertResultHasNoFailures(result);
    }
}
