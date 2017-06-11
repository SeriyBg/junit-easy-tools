package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import com.github.seriybg.junit.easytools.model.annotation.ProducedValue;
import org.junit.experimental.theories.ParameterSignature;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Bishyr
 */
abstract class BasicAssignment<T extends AccessibleObject & Member> implements Assignment {

    private static final Map<Type, Class<?>> primitiveSupplierToType = createPrimitiveMappings();

    private static Map<Type,Class<?>> createPrimitiveMappings() {
        Map<Type, Class<?>> map = new HashMap<>();
        map.put(Integer.class, Integer.TYPE);
        map.put(Long.class, Long.TYPE);
        map.put(Byte.class, Byte.TYPE);
        map.put(Boolean.class, Boolean.TYPE);
        map.put(Short.class, Short.TYPE);
        map.put(Character.class, Character.TYPE);
        map.put(Double.class, Double.TYPE);
        map.put(Float.class, Float.TYPE);
        return Collections.unmodifiableMap(map);
    }

    private final T assignmentProducer;

    BasicAssignment(T assignmentProducer) {
        this.assignmentProducer = assignmentProducer;
    }

    @Override
    public ParameterProducer parameterProducer() {
        try {
            return createParameterProducer();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    abstract ParameterProducer createParameterProducer() throws IllegalAccessException;

    boolean isValidGenericType(ParameterSignature parameterSignature, Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parametrizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
            Type actualTypeArgument = actualTypeArguments[0];
            if (parameterSignature.getType().equals(actualTypeArgument)
                    || parameterSignature.getType().equals(primitiveSupplierToType.get(actualTypeArgument))) {
                return true;
            }
        }
        return false;
    }

    boolean isProducerNameValid(ProducedValue paramAnnotation) {
        if (paramAnnotation == null) {
            return true;
        }
        DataProducer annotation = assignmentProducer.getAnnotation(DataProducer.class);
        String dataProducerName = annotation.name().isEmpty() ? assignmentProducer.getName() : annotation.name();
        return paramAnnotation.producer().equals(dataProducerName);
    }
}
