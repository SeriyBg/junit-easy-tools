package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.TestFactory;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author Serge Bishyr
 */
class TestObject {

    private final TestClass testClass;

    TestObject(TestClass testClass) {
        this.testClass = testClass;
    }

    Object createTestObject() throws Exception {
        List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(TestFactory.class);
        if (!annotatedMethods.isEmpty()) {
            Method factoryMethod = annotatedMethods.get(0).getMethod();
            validateFactoryMethod(factoryMethod);
            return factoryMethod.invoke(null);
        }
        return testClass.getOnlyConstructor().newInstance();
    }

    private void validateFactoryMethod(Method factoryMethod) throws InitializationError {
        if (!factoryMethod.getReturnType().isAssignableFrom(testClass.getJavaClass())) {
            throw new InitializationError("Illegal factory method return type.");
        }
        if (!Modifier.isStatic(factoryMethod.getModifiers())) {
            throw new InitializationError("@FactoryMethod must be static!");
        }
    }
}
