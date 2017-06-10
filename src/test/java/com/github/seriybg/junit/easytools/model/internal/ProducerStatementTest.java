package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import com.github.seriybg.junit.easytools.runner.JUnitEasyTools;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assume.assumeTrue;

/**
 * @author Serge Bishyr
 */
public class ProducerStatementTest {

    @RunWith(JUnitEasyTools.class)
    public static class ExceptionOnNoAssumption {

        private static final Queue<Integer> providedValues = new LinkedList<>();

        @DataProducer
        public static Supplier<Integer> stringSupplier = providedValues::poll;

        public static void fillInProvidedValues() {
            providedValues.add(-1);
            providedValues.add(-2);
            providedValues.add(-3);
        }

        @Ignore("Should not run as a single unit test")
        @Test
        @ProducedValues(iterations = 3)
        public void a(Integer i) {
            assumeTrue(i > 0);
        }

        @Ignore("Should not run as a single unit test")
        @Test
        @ProducedValues(iterations = 3)
        public void b(Integer i) {
            assumeTrue(i == -1);
        }
    }

    @Before
    public void prepareTestClass() throws Exception {
        ExceptionOnNoAssumption.fillInProvidedValues();
    }

    @Test
    public void shouldThrowExceptionIfAllTheAssumptionsFails() throws Throwable {
        TestClass testClass = new TestClass(ExceptionOnNoAssumption.class);
        FrameworkMethod method =
                new FrameworkMethod(ExceptionOnNoAssumption.class.getMethod("a", Integer.class));
        ProducerStatement producerStatement = new ProducerStatement(testClass, method);

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(producerStatement::evaluate);
    }

    @Test
    public void shouldNotThrowExceptionIfAtLeastOneAssumptionPass() throws Throwable {
        TestClass testClass = new TestClass(ExceptionOnNoAssumption.class);
        FrameworkMethod method =
                new FrameworkMethod(ExceptionOnNoAssumption.class.getMethod("b", Integer.class));
        ProducerStatement producerStatement = new ProducerStatement(testClass, method);

        producerStatement.evaluate();
    }
}