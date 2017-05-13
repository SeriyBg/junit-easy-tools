package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValue;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Serge Bishyr
 */
class MethodResultAssignment extends BasicAssignment<Method> {

    private final Object value;
    private final Type type;

    MethodResultAssignment(Object value, Type type, FrameworkMethod method) {
        super(method.getMethod());
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean isValidFor(ParameterSignature parameterSignature) {
        return isProducerNameValid(parameterSignature.getAnnotation(ProducedValue.class))
                && isValidGenericType(parameterSignature, type);
    }

    @Override
    ParameterProducer createParameterProducer() throws IllegalAccessException {
        return new ParameterProducer(((ParameterizedType)type).getRawType(), value);
    }
}
