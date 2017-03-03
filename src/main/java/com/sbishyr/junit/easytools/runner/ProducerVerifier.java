package com.sbishyr.junit.easytools.runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * Created by Serge Bishyr
 */
class ProducerVerifier extends BlockJUnit4ClassRunner {

    private FrameworkMethod method;
    private Object[] args;

    public ProducerVerifier(TestClass testClass, FrameworkMethod method, Object[] args) throws InitializationError {
        super(testClass.getJavaClass());
        this.method = method;
        this.args = args;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                method.invokeExplosively(test, args);
            }
        };
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Do nothing
    }

    public void evaluate() throws Throwable {
        methodBlock(method).evaluate();
    }

}
