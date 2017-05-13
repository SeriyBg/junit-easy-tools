package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValue;
import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.experimental.theories.ParameterSignature;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Serge Bishyr
 */
abstract class BasicAssignment<T extends AccessibleObject & Member> implements Assignment {

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
            if (parameterSignature.getType().equals(actualTypeArgument)) {
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
