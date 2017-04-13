package com.sbishyr.junit.easytools.model.internal;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serge Bishyr
 */
class ProducerAssignments {

    private final TestClass testClass;

    private final List<ParameterProducer> assigned;

    private final List<ParameterSignature> unassigned;

    private ProducerAssignments(
            TestClass testClass, List<ParameterProducer> assigned, List<ParameterSignature> unassigned) {
        this.testClass = testClass;
        this.assigned = assigned;
        this.unassigned = unassigned;
    }

    static ProducerAssignments allUnassigned(TestClass testClass, Method method) {
        ArrayList<ParameterSignature> signatures = ParameterSignature.signatures(method);
        return new ProducerAssignments(testClass, new ArrayList<>(), signatures);
    }

    boolean isComplete() {
        return unassigned.isEmpty();
    }


    List<ParameterProducer> potentialNextParameterProducers() {
        ParameterSignature next = nextUnassigned();
        return new PotentialAssignments(testClass).allPossible()
                .stream()
                .filter(assignmentObject -> assignmentObject.isValidFor(next))
                .map(AssignmentObject::parameterProducer)
                .collect(Collectors.toList());
    }

    private ParameterSignature nextUnassigned() {
        return unassigned.get(0);
    }


    ProducerAssignments assignNext(ParameterProducer parameterProducer) {
        List<ParameterProducer> newAssigned = new ArrayList<>(assigned);
        newAssigned.add(parameterProducer);
        return new ProducerAssignments(testClass, newAssigned, unassigned.subList(1, unassigned.size()));
    }

    List<ParameterProducer> getAssignedParams() {
        return assigned;
    }
}
