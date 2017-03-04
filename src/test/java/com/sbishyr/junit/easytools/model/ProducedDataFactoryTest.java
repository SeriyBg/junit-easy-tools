package com.sbishyr.junit.easytools.model;

import com.sbishyr.junit.easytools.runner.ProducedValue;
import org.junit.Test;
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

    public static class OneIntSupplier {
        @DataProducer
        public static IntSupplier producer = () -> 42;

        public void a(int i) {
            //Do nothing
        }

        public void b(String s) {
            fail("Method should not run!");
        }
    }

    public static class TwoSuppliersDifferentType {
        @DataProducer
        public static Supplier<String> stringProducer = () -> "42";

        @DataProducer
        public static Supplier<Integer> integerProducer = () -> 42;

        public void a(Integer i, String s) {
            //Do nothing
        }
    }

    public static class NamedDataProducers {

        @DataProducer(name = "first")
        public static Supplier<String> firstProducer = () -> "42";

        @DataProducer(name = "second")
        public static Supplier<String> secondProducer = () -> "27";

        public void a(@ProducedValue(producer = "second") String s) {
            //Do nothing
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
        TestClass testClass = new TestClass(OneIntSupplier.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("b", String.class));

        try {
            new ProducedDataFactory().getParams(testClass, method);
            fail("InitializationError expected");
        } catch (InitializationError e) {
            //Do nothing. Exception is expected.
        }
    }

    @Test
    public void shouldGetValueFromNamedDataProducer() throws Exception {
        TestClass testClass = new TestClass(NamedDataProducers.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", String.class));

        Object[] params = new ProducedDataFactory().getParams(testClass, method);
        assertThat(params).isEqualTo(new Object[]{"27"});
    }
}