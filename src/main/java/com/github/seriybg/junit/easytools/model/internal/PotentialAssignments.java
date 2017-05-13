package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
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

        final List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(DataProducer.class);


        Stream<Assignment> methodAssignments = Stream.empty();
        for (FrameworkMethod annotatedMethod : annotatedMethods) {
            final Type methodReturnType = getMethodGenericReturnType(annotatedMethod.getMethod());
            methodAssignments = Stream.concat(methodAssignments, methodToStreamOfResults(annotatedMethod)
                    .map(value -> new MethodResultAssignment(value, methodReturnType, annotatedMethod)));
        }

        final List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
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

    private static Type getMethodGenericReturnType (Method method) throws InitializationError {
        final Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedTypeImpl) {
            final Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericReturnType).getActualTypeArguments();
            return actualTypeArguments[0];
        }
        throw new InitializationError(
                "Illegal return type of " + genericReturnType + " for method as @DataProducer");
    }
}
