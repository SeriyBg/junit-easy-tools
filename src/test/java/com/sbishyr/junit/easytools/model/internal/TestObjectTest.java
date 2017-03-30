package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.TestFactory;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Serge Bishyr
 */
public class TestObjectTest {

    private static class ClassWithFactoryMethod{

        private final String injected;

        private ClassWithFactoryMethod(String injected) {
            this.injected = injected;
        }

        @TestFactory
        public static ClassWithFactoryMethod factory() {
            return new ClassWithFactoryMethod("injected");
        }
    }

    @Test
    public void shouldCreateObjectUsingFactoryMethod() throws Exception {
        TestClass testClass = new TestClass(ClassWithFactoryMethod.class);
        TestObject testObject = new TestObject(testClass);
        Object result = testObject.createTestObject();
        assertThat(result).isInstanceOf(ClassWithFactoryMethod.class);
        assertThat(((ClassWithFactoryMethod)result).injected).isEqualTo("injected");
    }

    private static class WrongFactoryType {
        @TestFactory
        public static String factory() {
            throw new AssertionError("Should no be called");
        }
    }

    @Test
    public void shouldThrowExceptionOnWrongTypeOfFactory() throws Exception {
        TestClass testClass = new TestClass(WrongFactoryType.class);
        assertThatExceptionOfType(InitializationError.class)
                .isThrownBy(() -> new TestObject(testClass).createTestObject());
    }

    private static class NotStaticFactoryMethod {
        @TestFactory
        public NotStaticFactoryMethod factoryMethod() {
            return new NotStaticFactoryMethod();
        }
    }

    @Test
    public void shouldValidateFactoryMethodIsStatic() throws Exception {
        TestClass testClass = new TestClass(NotStaticFactoryMethod.class);
        assertThatExceptionOfType(InitializationError.class)
                .isThrownBy(() -> new TestObject(testClass).createTestObject());
    }
}