package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.DataProducer;
import com.sbishyr.junit.easytools.model.ProducedValues;
import com.sbishyr.junit.easytools.utils.ResultAssertions;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

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
        private static final Queue<String> expectedValues = new LinkedList<>();
        static {
            expectedValues.add("firstString");
            expectedValues.add("2String");
            expectedValues.add("thirdOne");
        }

        @DataProducer
        public static Supplier<String> stringSupplier = () -> providedValues.poll();

        @Test
        @ProducedValues(iterations = 3)
        public void a(String s) {
            assertThat(s).isEqualTo(expectedValues.poll());
            count.getAndIncrement();
        }
    }

    @Test
    public void shouldSupportRepeatableTestsRun() throws Exception {
        Result result = JUnitCore.runClasses(ComplexProducerClass.class);
        assertThat(ComplexProducerClass.count.get()).isEqualTo(3);
        ResultAssertions.assertResultHasNoFailures(result);
    }
}