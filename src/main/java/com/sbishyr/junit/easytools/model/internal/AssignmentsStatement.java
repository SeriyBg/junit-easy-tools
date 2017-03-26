package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.TestFactory;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * @author Serge Bishyr
 */
class AssignmentsStatement extends Statement {

    private final Class<?> testClass;

    private final FrameworkMethod method;

    private final ProducerAssignments assignments;

    private final int index;

    AssignmentsStatement(Class<?> testClass, FrameworkMethod method, ProducerAssignments assignments, int index) {
        this.testClass = testClass;
        this.method = method;
        this.assignments = assignments;
        this.index = index;
    }

    @Override
    public void evaluate() throws Throwable {
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

        new BlockJUnit4ClassRunner(testClass) {

            @Override
            protected void validateTestMethods(List<Throwable> errors) {
                //Do nothing
            }

            @Override
            protected void validateConstructor(List<Throwable> errors) {
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
                return new DataProducerTestName(method, params, index).name();
            }

            @Override
            protected Object createTest() throws Exception {
                List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(TestFactory.class);
                if (!annotatedMethods.isEmpty()) {
                    return annotatedMethods.get(0).getMethod().invoke(null);
                }
                return super.createTest();
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
