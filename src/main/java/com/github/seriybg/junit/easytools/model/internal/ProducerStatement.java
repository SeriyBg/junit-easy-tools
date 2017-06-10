package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author Serge Bishyr
 */
public class ProducerStatement extends Statement {

    private final TestClass testClass;
    private final FrameworkMethod method;
    private int success = 0;
    private final List<AssumptionViolatedException> failedAssumptions = new ArrayList<>();

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
            try {
                new AssignmentsStatement(testClass.getJavaClass(), method, assignments, i).evaluate();
                success++;
            } catch (AssumptionViolatedException e) {
                failedAssumptions.add(e);
            }
        }
        if (success == 0) {
            fail("Never found parameters that satisfied method assumptions.  Violated assumptions: "
                    + failedAssumptions);
        }
    }
}
