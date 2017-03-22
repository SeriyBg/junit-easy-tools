package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * @author Serge Bishyr
 */
class ProducerAssignments {

    private final TestClass testClass;

    private final List<ParameterProducer> assigned;

    private final List<ParameterSignature> unassigned;

    private static final Map<Class<?>, Class<?>> primitiveSupplierToType = createPrimitiveSuppliers();

    private static Map<Class<?>, Class<?>> createPrimitiveSuppliers() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(IntSupplier.class, Integer.TYPE);
        map.put(LongSupplier.class, Long.TYPE);
        map.put(BooleanSupplier.class, Boolean.TYPE);
        map.put(DoubleSupplier.class, Double.TYPE);
        return Collections.unmodifiableMap(map);
    }

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
        ParameterSignature next = nextUnassigned();

        List<ParameterProducer> result = new ArrayList<>();

        for (FrameworkField annotatedField : annotatedFields) {
            if (!isProducerNameValid(annotatedField, next.getAnnotation(ProducedValue.class))) {
                continue;
            }

            Class<?> type = annotatedField.getField().getType();
            Class<?> aClass = primitiveSupplierToType.get(type);
            if (aClass != null && aClass.equals(next.getType())) {
                result.add(new ParameterProducer(annotatedField));
            } else {
                Type genericType = annotatedField.getField().getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parametrizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (next.getType().equals(actualTypeArgument)) {
                        result.add(new ParameterProducer(annotatedField));
                    }
                }
            }
        }
        return result;
    }

    private boolean isProducerNameValid(FrameworkField annotatedField, ProducedValue paramAnnotation) {
        if (paramAnnotation == null) {
            return true;
        }
        DataProducer annotation = annotatedField.getAnnotation(DataProducer.class);
        String dataProducerName = annotation.name().isEmpty() ? annotatedField.getName() : annotation.name();
        return paramAnnotation.producer().equals(dataProducerName);
    }

    private ParameterSignature nextUnassigned() {
        return unassigned.get(0);
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
