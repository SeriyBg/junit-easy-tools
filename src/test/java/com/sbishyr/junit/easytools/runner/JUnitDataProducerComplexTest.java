package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.sbishyr.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Serge Bishyr on 3/4/17.
 */
public class JUnitDataProducerComplexTest {

    @RunWith(JUnitDataProducer.class)
    public static class ComplexProducerClass {

        //Not possible to use Result.getRunCount() because it counts unique method that has run.
        //But we need to check that the same method has run 3 times
        private static final AtomicInteger count = new AtomicInteger();

        private static final Queue<String> providedValues = new LinkedList<>();
        static {
            providedValues.add("firstString");
            providedValues.add("2String");
            providedValues.add("thirdOne");
        }

        //The copy the providedValues as long as we expect each value one by one to be provided
        private static final Queue<String> expectedValues = new LinkedList<>(providedValues);

        @DataProducer
        public static Supplier<String> stringSupplier = () -> providedValues.poll();

        @Test
        @ProducedValues(iterations = 3)
        public void a(String s) {
            assertThat(s).isEqualTo(expectedValues.poll());
            count.getAndIncrement();
        }
    }

    @RunWith(JUnitDataProducer.class)
    public static class NamedDataProducer {

        @DataProducer
        public static Supplier<String> firstSupplier = () -> "firstSupplier";

        @DataProducer(name = "usedSupplier")
        public static Supplier<String> secondSupplier = () -> "secondSupplier";

        @Test
        public void a(@ProducedValue(producer = "usedSupplier") String s) {
            assertThat(s).isEqualTo("secondSupplier");
        }

        @Test
        public void b(@ProducedValue(producer = "firstSupplier") String s) {
            assertThat(s).isEqualTo("firstSupplier");
        }


    }

    @Test
    public void shouldSupportRepeatableTestsRun() throws Exception {
        Result result = JUnitCore.runClasses(ComplexProducerClass.class);
        assertThat(ComplexProducerClass.count.get()).isEqualTo(3);
        assertResultHasNoFailures(result);
    }

    @Test
    public void shouldProvideValueFromNamedProducer() throws Exception {
        Result result = JUnitCore.runClasses(NamedDataProducer.class);
        assertThat(result.getRunCount()).isEqualTo(2);
        assertResultHasNoFailures(result);
    }
}