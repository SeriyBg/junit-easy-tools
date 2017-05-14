package com.github.seriybg.junit.easytools.runner;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.github.seriybg.junit.easytools.utils.ResultAssertions.assertResultHasNoFailures;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyToolsAssumptionsTest {

    @RunWith(JUnitEasyTools.class)
    public static class TestWithAssumptions {

        //Not possible to use Result.getRunCount() because it counts unique method that has run.
        //But we need to check that the same method has run 3 times
        private static final AtomicInteger count = new AtomicInteger();

        private static final Queue<String> providedValues = new LinkedList<>();

        @DataProducer
        public static Supplier<String> stringSupplier = providedValues::poll;

        @BeforeClass
        public static void setUpCounter() {
            count.set(0);
            providedValues.add("firstString");
            providedValues.add("2String");
            providedValues.add("thirdValue");
        }

        @Test
        @ProducedValues(iterations = 3)
        public void a(String s) {
            assumeThat(s, not(equalTo("2String")));
            if(s.equals("2String")) {
                fail("Should not get value that does not pass assumption.");
            }
            count.incrementAndGet();
        }
    }

    @Test
    public void shouldIgnoreRunningOfTestWithNotPassingAssumption() throws Exception {
        final Result result = JUnitCore.runClasses(TestWithAssumptions.class);
        assertResultHasNoFailures(result);
        assertThat(TestWithAssumptions.count.intValue()).isEqualTo(2);
    }
}