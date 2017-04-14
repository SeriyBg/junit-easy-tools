package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class PotentialAssignmentsTest {

    private PotentialAssignments assignments;

    public static class AssignmentsFromField {

        @DataProducer
        public static Supplier<String> fieldAssignment = () -> "value";

        public void a(String s) {
            //Do nothing
        }

        public void b(int i) {
            //Do nothing
        }
    }

    @Test
    public void shouldHaveAssignmentsFromField() throws Throwable {
        this.assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<Assignment> assignments = this.assignments.allPossible();
        assertThat(assignments).hasSize(1);

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromField.class.getMethod("a", String.class));

        Assignment assignment = assignments.get(0);
        assertThat(assignment.isValidFor(parameterSignature.get(0))).isTrue();
    }

    @Test
    public void shouldNotBeValidIfTypeDoesNotMatch() throws Throwable {
        this.assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<Assignment> assignments = this.assignments.allPossible();

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromField.class.getMethod("b", Integer.TYPE));
        Assignment assignment = assignments.get(0);
        assertThat(assignment.isValidFor(parameterSignature.get(0))).isFalse();
    }

    @Test
    public void shouldProduceValueFromProducer() throws Throwable {
        this.assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<Assignment> assignments = this.assignments.allPossible();

        Assignment assignment = assignments.get(0);
        assertThat(assignment.parameterProducer().produceParamValue()).isEqualTo("value");
    }

    public static class AssignmentsFromMethod {

        @DataProducer
        public static List<Supplier<String>> dataProducer() {
            return Arrays.asList(
                    () -> "42",
                    () -> "27");
        }

        public void a(String s) {
            //Do nothing
        }
    }

    @Test
    public void shouldFindPossibleAssignmentsFromMethod() throws Throwable {
        this.assignments = new PotentialAssignments(new TestClass(AssignmentsFromMethod.class));
        final List<Assignment> assignments = this.assignments.allPossible();

        assertThat(assignments).hasSize(2);

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromMethod.class.getMethod("a", String.class));

        assertThat(assignments.get(0).isValidFor(parameterSignature.get(0)));
        assertThat(assignments.get(0).parameterProducer().produceParamValue()).isEqualTo("42");

        assertThat(assignments.get(1).isValidFor(parameterSignature.get(0)));
        assertThat(assignments.get(1).parameterProducer().produceParamValue()).isEqualTo("27");
    }
}