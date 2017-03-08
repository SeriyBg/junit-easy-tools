package com.sbishyr.junit.easytools.model;

import org.junit.runners.model.FrameworkField;

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * @author by Serge Bishyr
 */
class ParameterProducer {

    private final Class<?> type;

    private final FrameworkField field;

    ParameterProducer(Class<?> type, FrameworkField field) {
        this.type = type;
        this.field = field;
    }

    private static final Map<Class<?>, Function<Object, Object>> classToProducer = new HashMap<>();
    static {
        classToProducer.put(Supplier.class, s -> ((Supplier<?>)s).get());
        classToProducer.put(IntSupplier.class, s -> ((IntSupplier)s).getAsInt());
        classToProducer.put(LongSupplier.class, s -> ((LongSupplier)s).getAsLong());
        classToProducer.put(BooleanSupplier.class, s -> ((BooleanSupplier)s).getAsBoolean());
        classToProducer.put(DoubleSupplier.class, s -> ((DoubleSupplier)s).getAsDouble());
    }
    Object produceParamValue() throws IllegalAccessException {
        return classToProducer.get(type).apply(field.get(null));
    }
}
