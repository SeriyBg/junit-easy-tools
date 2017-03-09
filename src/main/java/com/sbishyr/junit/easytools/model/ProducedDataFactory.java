package com.sbishyr.junit.easytools.model;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

/**
 * Created by Serge Bishyr
 */
class ProducedDataFactory {

    Object[] getParams(TestClass testClass, FrameworkMethod method)
            throws IllegalAccessException, InitializationError {
        List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
        Parameter[] parameters = method.getMethod().getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            params[i] = getParamValue(parameter, annotatedFields);
        }
        return params;
    }

    private static final Map<Class<?>, Class<?>> primitiveSupplierToType;
    static {
        primitiveSupplierToType = new HashMap<>();
        primitiveSupplierToType.put(IntSupplier.class, Integer.TYPE);
        primitiveSupplierToType.put(LongSupplier.class, Long.TYPE);
        primitiveSupplierToType.put(BooleanSupplier.class, Boolean.TYPE);
        primitiveSupplierToType.put(DoubleSupplier.class, Double.TYPE);
    }

    private static final Map<Class<?>, Function<Object, Object>> classToProducer = new HashMap<>();
    static {
        classToProducer.put(Supplier.class, s -> ((Supplier<?>)s).get());
        classToProducer.put(IntSupplier.class, s -> ((IntSupplier)s).getAsInt());
        classToProducer.put(LongSupplier.class, s -> ((LongSupplier)s).getAsLong());
        classToProducer.put(BooleanSupplier.class, s -> ((BooleanSupplier)s).getAsBoolean());
        classToProducer.put(DoubleSupplier.class, s -> ((DoubleSupplier)s).getAsDouble());
    }
    private Object calculatePramValue(Class<?> type, FrameworkField annotatedField) throws IllegalAccessException {
        return classToProducer.get(type).apply(annotatedField.get(null));
    }

    private Object getParamValue(Parameter parameter, List<FrameworkField> fields)
            throws IllegalAccessException, InitializationError {

        for (FrameworkField annotatedField : fields) {
            ProducedValue producedValue = parameter.getAnnotation(ProducedValue.class);
            if (!isProducerNameValid(annotatedField, producedValue)) {
                continue;
            }

            Class<?> type = annotatedField.getField().getType();
            Class<?> aClass = primitiveSupplierToType.get(type);
            if (aClass != null && aClass.equals(parameter.getType())) {
                return calculatePramValue(type, annotatedField);
            } else {
                Type genericType = annotatedField.getField().getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parametrizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (parameter.getType().equals(actualTypeArgument)) {
                        return calculatePramValue(type, annotatedField);
                    }
                }
            }
        }
        throw new InitializationError("No @DataProducer found for parameter "
                                                + parameter.getType() + "  " + parameter.getName());
    }

    private boolean isProducerNameValid(FrameworkField annotatedField, ProducedValue paramAnnotation) {
        if (paramAnnotation == null) {
            return true;
        }
        DataProducer annotation = annotatedField.getAnnotation(DataProducer.class);
        String dataProducerName = annotation.name().isEmpty() ? annotatedField.getName() : annotation.name();
        return paramAnnotation.producer().equals(dataProducerName);
    }
}
