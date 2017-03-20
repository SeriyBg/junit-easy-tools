package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * @author Serge Bishyr
 */
public class ProducerVerifier extends BlockJUnit4ClassRunner {

    private FrameworkMethod method;

    public ProducerVerifier(TestClass testClass, FrameworkMethod method) throws InitializationError {
        super(testClass.getJavaClass());
        this.method = method;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new ProducerVerifierStatement(method);
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        //Do nothing
    }

    public void evaluate() throws Throwable {
        ProducedValues marker = method.getAnnotation(ProducedValues.class);
        if (marker != null) {
            for (int i = 0; i < marker.iterations(); i++) {
                methodBlock(method).evaluate();
            }
        } else {
            methodBlock(method).evaluate();
        }
    }

    private class ProducerVerifierStatement extends Statement {
        private final FrameworkMethod method;

        private ProducerVerifierStatement(FrameworkMethod method) {
            this.method = method;
        }

        @Override
        public void evaluate() throws Throwable {
            ProducerAssignments assignments = ProducerAssignments.allUnassigned(getTestClass(), method.getMethod());
            runWithAssignments(assignments);
        }

        private void runWithAssignments(ProducerAssignments assignments) throws Throwable {
            if (!assignments.isComplete()) {
                runWithIncompleteAssignments(assignments);
            } else {
                runWithCompleteAssignments(assignments);
            }
        }

        private void runWithIncompleteAssignments(ProducerAssignments assignments) throws Throwable {
            for (ParameterProducer parameterProducer : assignments.potentialNextParameterProducers()) {
                runWithAssignments(assignments.assignNext(parameterProducer));
            }
        }

        private void runWithCompleteAssignments(ProducerAssignments assignments) throws Throwable {
            Object[] params = assignments.getAssignedParams().stream()
                    .map(this::produceParamValue)
                    .toArray();

            new BlockJUnit4ClassRunner(getTestClass().getJavaClass()) {

                @Override
                protected void validateTestMethods(List<Throwable> errors) {
                    //Do nothing
                }

                @Override
                protected Statement methodInvoker(FrameworkMethod method, Object test) {
                    return new Statement() {
                        @Override
                        public void evaluate() throws Throwable {
                            method.invokeExplosively(test, params);
                        }
                    };
                }

                @Override
                protected Statement methodBlock(FrameworkMethod method) {
                    return super.methodBlock(method);
                }

                @Override
                protected String testName(FrameworkMethod method) {
                    return new DataProducerTestName(method, params).name();
                }
            }.methodBlock(method).evaluate();
        }

        private Object produceParamValue(ParameterProducer parameterProducer) {
            try {
                return parameterProducer.produceParamValue();
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

    }
}
