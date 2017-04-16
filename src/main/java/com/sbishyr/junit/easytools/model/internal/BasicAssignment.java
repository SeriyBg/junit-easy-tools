package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.experimental.theories.ParameterSignature;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Serge Bishyr
 */
abstract class BasicAssignment<T extends AccessibleObject & Member> implements Assignment {

    private final T accessibleObject;

    BasicAssignment(T accessibleObject) {
        this.accessibleObject = accessibleObject;
    }

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
        DataProducer annotation = accessibleObject.getAnnotation(DataProducer.class);
        String dataProducerName = annotation.name().isEmpty() ? accessibleObject.getName() : annotation.name();
        return paramAnnotation.producer().equals(dataProducerName);
    }
}
