package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkField;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * @author Serge Bishyr
 */
class FieldAssignment implements Assignment {

    private static final Map<Class<?>, Class<?>> primitiveSupplierToType = createPrimitiveSuppliers();

    private static Map<Class<?>, Class<?>> createPrimitiveSuppliers() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(IntSupplier.class, Integer.TYPE);
        map.put(LongSupplier.class, Long.TYPE);
        map.put(BooleanSupplier.class, Boolean.TYPE);
        map.put(DoubleSupplier.class, Double.TYPE);
        return Collections.unmodifiableMap(map);
    }

    private final FrameworkField field;

    FieldAssignment(FrameworkField field) {
        this.field = field;
    }

    @Override
    public boolean isValidFor(ParameterSignature parameterSignature) {
        if (!isProducerNameValid(parameterSignature.getAnnotation(ProducedValue.class))) {
            return false;
        }
        Class<?> type = field.getField().getType();
        Class<?> aClass = primitiveSupplierToType.get(type);
        if (aClass != null && aClass.equals(parameterSignature.getType())) {
            return true;
        } else {
            Type genericType = field.getField().getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parametrizedType = (ParameterizedType) genericType;
                Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
                Type actualTypeArgument = actualTypeArguments[0];
                if (parameterSignature.getType().equals(actualTypeArgument)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ParameterProducer parameterProducer() {
        try {
            return new ParameterProducer(field.getType(), field.get(null));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean isProducerNameValid(ProducedValue paramAnnotation) {
        if (paramAnnotation == null) {
            return true;
        }
        DataProducer annotation = field.getAnnotation(DataProducer.class);
        String dataProducerName = annotation.name().isEmpty() ? field.getName() : annotation.name();
        return paramAnnotation.producer().equals(dataProducerName);
    }
}
