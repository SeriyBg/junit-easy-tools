package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.ProducerVerifier;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * @author by Serge Bishyr
 */
public class JUnitDataProducer extends BlockJUnit4ClassRunner {

    public JUnitDataProducer(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        if (method.getMethod().getParameterCount() == 0) {
            return super.methodBlock(method);
        }
        return new DataProducedStatement(method, getTestClass());
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(Test.class);
        for (FrameworkMethod annotatedMethod : annotatedMethods) {
            annotatedMethod.validatePublicVoid(false, errors);
        }
    }

    private static class DataProducedStatement extends Statement {

        private final FrameworkMethod frameworkMethod;
        private final TestClass testClass;

        DataProducedStatement(FrameworkMethod frameworkMethod, TestClass testClass) {
            this.frameworkMethod = frameworkMethod;
            this.testClass = testClass;
        }

        @Override
        public void evaluate() throws Throwable {
            new ProducerVerifier(testClass, frameworkMethod).evaluate();
        }
    }
}
