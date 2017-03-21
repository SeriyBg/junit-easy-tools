package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * @author Serge Bishyr
 */
public class ProducerStatement extends Statement {

    private final TestClass testClass;
    private final FrameworkMethod method;

    public ProducerStatement(TestClass testClass, FrameworkMethod method) {
        this.testClass = testClass;
        this.method = method;
    }

    @Override
    public void evaluate() throws Throwable {
        ProducedValues marker = method.getAnnotation(ProducedValues.class);
        ProducerAssignments assignments = ProducerAssignments.allUnassigned(testClass, method.getMethod());
        int iterations = marker == null ? 1 : marker.iterations();
        for (int i = 0; i < iterations; i++) {
            new AssignmentsStatement(testClass.getJavaClass(), method, assignments, i).evaluate();
        }
    }
}
