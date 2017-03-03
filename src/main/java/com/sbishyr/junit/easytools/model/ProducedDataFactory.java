package com.sbishyr.junit.easytools.model;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Serge Bishyr
 */
public class ProducedDataFactory {

    public Object[] getParams(TestClass testClass, FrameworkMethod method) throws IllegalAccessException {
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
    }

    private static final Map<Class<?>, Function<Object, Object>> classToProducer = new HashMap<>();
    static {
        classToProducer.put(IntSupplier.class, s -> ((IntSupplier)s).getAsInt());
        classToProducer.put(Supplier.class, s -> ((Supplier<?>)s).get());
    }
    private Object getParamValue(Class<?> paramType, List<FrameworkField> fields) throws IllegalAccessException {
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
        return null;
    }
}
