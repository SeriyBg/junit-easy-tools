package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
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
    public void shouldHaveAssignmentsFromField() throws Exception {
        assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<AssignmentObject> assignmentObjects = assignments.allPossible();
        assertThat(assignmentObjects).hasSize(1);

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromField.class.getMethod("a", String.class));

        AssignmentObject assignmentObject = assignmentObjects.get(0);
        assertThat(assignmentObject.isValidFor(parameterSignature.get(0))).isTrue();
    }

    @Test
    public void shouldNotBeValidIfTypeDoesNotMatch() throws Exception {
        assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<AssignmentObject> assignmentObjects = assignments.allPossible();

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromField.class.getMethod("b", Integer.TYPE));
        AssignmentObject assignmentObject = assignmentObjects.get(0);
        assertThat(assignmentObject.isValidFor(parameterSignature.get(0))).isFalse();
    }

    @Test
    public void shouldProduceValueFromProducer() throws Exception {
        assignments = new PotentialAssignments(new TestClass(AssignmentsFromField.class));
        List<AssignmentObject> assignmentObjects = assignments.allPossible();

        ArrayList<ParameterSignature> parameterSignature =
                ParameterSignature.signatures(AssignmentsFromField.class.getMethod("a", String.class));

        AssignmentObject assignmentObject = assignmentObjects.get(0);
        assertThat(assignmentObject.produceParamValue()).isEqualTo("value");
    }
}