package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

/**
 * @author by Serge Bishyr
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

    public static class NotSupportedDataProducerType {
        @DataProducer
        public static String strinSupplier = "notSupported";

        public void a(String s) {
            //Do nothing
        }
    }

    public static class MultiplePossibleParam {
        @DataProducer
        public static Supplier<String> firstProducer = () -> "42";

        @DataProducer
        public static Supplier<String> secondProducer = () -> "27";

        public void a(String s) {
            //Do nothing
        }
    }

    @Test
    public void shouldSupplyInt() throws Exception {
        TestClass testClass = new TestClass(OneIntSupplier.class);
        FrameworkMethod method = new FrameworkMethod(testClass.getJavaClass().getMethod("a", Integer.TYPE));

        List<Object[]> producedParams =
                new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class)).getParamsSequence();
        assertThat(producedParams).containsExactly(new Object[]{42});
    }

    @Test
    public void shouldSupplyTwoValues() throws Exception {
        TestClass testClass = new TestClass(TwoSuppliersDifferentType.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", Integer.class, String.class));

        List<Object[]> producedParams =
                new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class)).getParamsSequence();
        assertThat(producedParams).containsExactly(new Object[]{42, "42"});
    }

    @Test
    public void shouldThrowInitializationErrorIfNoDataProducerOfTypeFound() throws Exception {
        TestClass testClass = new TestClass(OneIntSupplier.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("b", String.class));

        assertThatExceptionOfType(InitializationError.class)
                .isThrownBy(() ->
                        new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class))
                                .getParamsSequence());
    }

    @Test
    public void shouldGetValueFromNamedDataProducer() throws Exception {
        TestClass testClass = new TestClass(NamedDataProducers.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", String.class));

        List<Object[]> params =
                new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class)).getParamsSequence();

        assertThat(params).containsExactly(new Object[]{"27"});
    }

    @Test
    public void shouldGetExceptionForNotSupportedSupplierType() throws Exception {
        TestClass testClass = new TestClass(NotSupportedDataProducerType.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", String.class));

        assertThatExceptionOfType(InitializationError.class)
                .isThrownBy(() ->
                        new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class))
                                .getParamsSequence());
    }

    @Test
    public void shouldGetAllPossibleParameters() throws Exception {
        TestClass testClass = new TestClass(MultiplePossibleParam.class);
        FrameworkMethod method = new FrameworkMethod(
                testClass.getJavaClass().getMethod("a", String.class));

        List<Object[]> paramsSequence =
                new ProducedDataFactory(method, testClass.getAnnotatedFields(DataProducer.class)).getParamsSequence();

        assertThat(paramsSequence).containsExactly(new Object[]{"42"}, new Object[]{"27"});
    }
}