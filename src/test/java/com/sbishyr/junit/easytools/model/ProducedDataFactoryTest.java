package com.sbishyr.junit.easytools.model;

import com.sbishyr.junit.easytools.runner.JUnitDataProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by Serge Bishyr on 3/3/17.
 */
public class ProducedDataFactoryTest {

    @RunWith(JUnitDataProducer.class)
    public static class OneIntSupplier {
        @DataProducer
        public static IntSupplier producer = () -> 42;

        @Test public void a(int i) {
            assertThat(i).isEqualTo(42);
        }
    }

    @RunWith(JUnitDataProducer.class)
    public static class TwoSuppliersDifferentType {
        @DataProducer
        public static Supplier<String> stringProducer = () -> "42";

        @DataProducer
        public static Supplier<Integer> integerProducer = () -> 42;

        @Test public void a(Integer i, String s) {
            assertThat(i).isEqualTo(42);
            assertThat(s).isEqualTo("42");
        }
    }

    public static class NotInitializedCorrectly {
        @DataProducer
        public static Supplier<String> stringSupplier = () -> "string";

        public void a(int i) {
            fail("Method should not run!");
        }
    }

    @Test
    public void shouldSupplyInt() throws Exception {
        TestClass testClass = new TestClass(OneIntSupplier.class);
        FrameworkMethod method = new FrameworkMethod(testClass.getJavaClass().getMethod("a", Integer.TYPE));

        Object[] producedParams = new ProducedDataFactory().getParams(testClass, method);
        assertThat(producedParams).isEqualTo(new Object[]{42});
    }

    @Test
    public void shouldSupplyTwoValues() throws Exception {
        TestClass testClass = new TestClass(TwoSuppliersDifferentType.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", Integer.class, String.class));

        Object[] producedParams = new ProducedDataFactory().getParams(testClass, method);
        assertThat(producedParams).isEqualTo(new Object[]{42, "42"});
    }

    @Test
    public void shouldThrowInitializationErrorIfNoDataProducerOfTypeFound() throws Exception {
        TestClass testClass = new TestClass(NotInitializedCorrectly.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", Integer.TYPE));

        try {
            new ProducedDataFactory().getParams(testClass, method);
            fail("InitializationError expected");
        } catch (InitializationError e) {
            //Do nothing. Exception is expected!
        }
    }
}