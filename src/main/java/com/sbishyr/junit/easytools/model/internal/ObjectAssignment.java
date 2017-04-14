package com.sbishyr.junit.easytools.model.internal;

import org.junit.experimental.theories.ParameterSignature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Serge Bishyr
 */
class ObjectAssignment implements Assignment {

    private final Object value;
    private final Type type;

    ObjectAssignment(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean isValidFor(ParameterSignature parameterSignature) {
//        if (!isProducerNameValid(parameterSignature.getAnnotation(ProducedValue.class))) {
//            return false;
//        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parametrizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
            Type actualTypeArgument = actualTypeArguments[0];
            if (parameterSignature.getType().equals(actualTypeArgument)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ParameterProducer parameterProducer() {
        return new ParameterProducer(((ParameterizedType)type).getRawType(), value);
    }
}
