package com.sbishyr.junit.easytools.model;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * Created by Serge Bishyr
 */
public class ProducerVerifier extends BlockJUnit4ClassRunner {

    private FrameworkMethod method;

    public ProducerVerifier(TestClass testClass, FrameworkMethod method) throws InitializationError {
        super(testClass.getJavaClass());
        this.method = method;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new ProducerVerifierStatement(method, test);
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        //Do nothing
    }

    public void evaluate() throws Throwable {
        methodBlock(method).evaluate();
    }

    private class ProducerVerifierStatement extends Statement {
        private final FrameworkMethod method;
        private final Object test;

        public ProducerVerifierStatement(FrameworkMethod method, Object test) {
            this.method = method;
            this.test = test;
        }

        @Override
        public void evaluate() throws Throwable {
            ProducedValues marker = method.getAnnotation(ProducedValues.class);
            if (marker != null) {
                for (int i = 0; i < marker.iterations(); i++) {
                    invokeMethodExplosively();
                }
            } else {
                invokeMethodExplosively();
            }
        }

        private void invokeMethodExplosively() throws Throwable {
            Object[] args = new ProducedDataFactory().getParams(getTestClass(), method);
            method.invokeExplosively(test, args);
        }
    }
}
