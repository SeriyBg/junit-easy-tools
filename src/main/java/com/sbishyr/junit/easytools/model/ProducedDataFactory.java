package com.sbishyr.junit.easytools.model;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

/**
 * Created by Serge Bishyr
 */
public class ProducedDataFactory {

    public Object[] getParams(TestClass testClass, FrameworkMethod method)
            throws IllegalAccessException, InitializationError {
        List<FrameworkField> annotatedFields = testClass.getAnnotatedFields(DataProducer.class);
        Class<?>[] parameterTypes = method.getMethod().getParameterTypes();
        Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            params[i] = getParamValue(parameterType, annotatedFields);
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
    private Object getParamValue(Class<?> paramType, List<FrameworkField> fields)
            throws IllegalAccessException, InitializationError {
        for (FrameworkField annotatedField : fields) {
            Class<?> type = annotatedField.getField().getType();
            Class<?> aClass = primitiveSupplierToType.get(type);
            if (aClass != null && aClass.equals(paramType)) {
                return classToProducer.get(type).apply(annotatedField.get(null));
            } else {
                Type genericType = annotatedField.getField().getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parametrizedType = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (paramType.equals(actualTypeArgument)) {
                        return classToProducer.get(type).apply(annotatedField.get(null));
                    }
                }
            }
        }
        throw new InitializationError("No @DataProducer found for type " + paramType);
    }
}
