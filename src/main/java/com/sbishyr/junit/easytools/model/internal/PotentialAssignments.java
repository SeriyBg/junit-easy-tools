package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Serge Bishyr
 */
class PotentialAssignments {

    private final TestClass testClass;

    PotentialAssignments(TestClass testClass) {
        this.testClass = testClass;
    }

    Stream<Assignment> allPossible() throws Throwable {
        final List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
        final List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(DataProducer.class);

        final Type methodReturnType = annotatedMethods.stream()
                .map(FrameworkMethod::getMethod)
                .findFirst()
                .map(Method::getGenericReturnType)
                .map(ParameterizedTypeImpl.class::cast)
                .map(ParameterizedTypeImpl::getActualTypeArguments)
                .map(array -> array[0])
                .orElse(null);

        Stream<Assignment> methodAssignments = Stream.empty();
        for (FrameworkMethod annotatedMethod : annotatedMethods) {
            methodAssignments = Stream.concat(methodAssignments, methodToStreamOfResults(annotatedMethod)
                    .map(value -> new MethodResultAssignment(value, methodReturnType, annotatedMethod)));
        }

        final Stream<Assignment> fieldsAssignments = annotatedFields.stream()
                .map(FieldAssignment::new);
        return Stream.concat(fieldsAssignments, methodAssignments);
    }

    private static Stream<?> methodToStreamOfResults(FrameworkMethod annotatedMethod) {
        try {
            return ((Collection<?>) annotatedMethod.invokeExplosively(null)).stream();
        } catch (Throwable throwable) {
            throw new IllegalArgumentException(throwable);
        }
    }
}
