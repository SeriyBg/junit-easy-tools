package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * @author by Serge Bishyr
 */
class ProducedDataFactory {

    private final List<FrameworkField> annotatedFields;
    private final FrameworkMethod method;

    ProducedDataFactory(FrameworkMethod method, List<FrameworkField> annotatedFields) {
        this.annotatedFields = annotatedFields;
        this.method = method;
    }

    List<Object[]> getParamsSequence() throws InitializationError, IllegalAccessException {
        List<Object[]> paramsSequence = new ArrayList<>();
        paramsSequence.add(getParams());
        return paramsSequence;
    }

    private Object[] getParams() throws InitializationError, IllegalAccessException {
        Parameter[] parameters = method.getMethod().getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            params[i] = getParamValue(parameter);
        }
        return params;
    }

    private static final Map<Class<?>, Class<?>> primitiveSupplierToType = new HashMap<>();
    static {
        primitiveSupplierToType.put(IntSupplier.class, Integer.TYPE);
        primitiveSupplierToType.put(LongSupplier.class, Long.TYPE);
        primitiveSupplierToType.put(BooleanSupplier.class, Boolean.TYPE);
        primitiveSupplierToType.put(DoubleSupplier.class, Double.TYPE);
    }

    private Object getParamValue(Parameter parameter)
            throws IllegalAccessException, InitializationError {

        for (FrameworkField annotatedField : annotatedFields) {
            ProducedValue producedValue = parameter.getAnnotation(ProducedValue.class);
            if (!isProducerNameValid(annotatedField, producedValue)) {
                continue;
            }

            Class<?> type = annotatedField.getField().getType();
            Class<?> aClass = primitiveSupplierToType.get(type);
            if (aClass != null && aClass.equals(parameter.getType())) {
                return new ParameterProducer(annotatedField).produceParamValue();
            } else {
                Type genericType = annotatedField.getField().getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parametrizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (parameter.getType().equals(actualTypeArgument)) {
                        return new ParameterProducer(annotatedField).produceParamValue();
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
