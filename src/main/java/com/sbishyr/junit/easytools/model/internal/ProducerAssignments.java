package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by Serge Bishyr
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

    public boolean isComplete() {
        return unassigned.isEmpty();
    }

    public List<ParameterProducer> potentialNextParameterProducers() {
        List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
        return annotatedFields.stream()
                .map(field -> new ParameterProducer(field))
                .collect(Collectors.toList());
    }

    public ProducerAssignments assignNext(ParameterProducer parameterProducer) {
        List<ParameterProducer> newAssigned = new ArrayList<>(assigned);
        newAssigned.add(parameterProducer);
        return new ProducerAssignments(testClass, newAssigned, unassigned.subList(1, unassigned.size()));
    }

    public List<ParameterProducer> getAssignedParams() {
        return assigned;
    }
}
