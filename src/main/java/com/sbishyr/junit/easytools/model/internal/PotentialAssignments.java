package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serge Bishyr
 */
class PotentialAssignments {

    private final TestClass testClass;

    PotentialAssignments(TestClass testClass) {
        this.testClass = testClass;
    }

    List<AssignmentObject> allPossible() {
        final List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
        final List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(DataProducer.class);

        return annotatedFields.stream().map(FieldAssignmentObject::new).collect(Collectors.toList());
    }

}
