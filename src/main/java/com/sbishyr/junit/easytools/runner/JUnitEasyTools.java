package com.sbishyr.junit.easytools.runner;

import com.sbishyr.junit.easytools.model.internal.ProducerStatement;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * @author Serge Bishyr
 */
public class JUnitEasyTools extends BlockJUnit4ClassRunner {

    public JUnitEasyTools(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        return new DataProducedStatement(method, getTestClass());
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(Test.class);
        for (FrameworkMethod annotatedMethod : annotatedMethods) {
            annotatedMethod.validatePublicVoid(false, errors);
        }
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        //Do nothing
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
            new ProducerStatement(testClass, frameworkMethod).evaluate();
        }
    }
}
