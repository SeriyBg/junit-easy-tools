package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        List<ParameterProducer> parameterProducers = assignments.potentialNextParameterProducers();
        if (parameterProducers.isEmpty()) {
            throw new InitializationError(
                    "No @DataProducer found to inject to suite method " + method.getName());
        }
        for (ParameterProducer parameterProducer : parameterProducers) {
            runWithAssignments(assignments.assignNext(parameterProducer));
        }
    }

    private void runWithCompleteAssignments(ProducerAssignments assignments) throws Throwable {
        Object[] params = assignments.getAssignedParams().stream()
                .map(ParameterProducer::produceParamValue)
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
                        validateParams();
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
                return new TestObject(getTestClass()).createTestObject();
            }

            private void validateParams() {
                final ProducedValues marker = method.getAnnotation(ProducedValues.class);
                if (marker != null
                        && !marker.nullsAccepted() && Arrays.stream(params).anyMatch(Objects::isNull)) {
                    throw new AssumptionViolatedException(
                            "Nulls not allowed as parameter for test method: " + method.getName());
                }
            }

        }.methodBlock(method).evaluate();
    }

}
