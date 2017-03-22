package com.sbishyr.junit.easytools.model.internal;

import org.junit.runners.model.FrameworkField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * @author Serge Bishyr
 */
class ParameterProducer {


    private final FrameworkField field;

    private static final Map<Class<?>, Function<Object, Object>> classToProducer = createClassToProducer();

    private static Map<Class<?>, Function<Object, Object>> createClassToProducer() {
        Map<Class<?>, Function<Object, Object>> map = new HashMap<>();
        map.put(Supplier.class, s -> ((Supplier<?>)s).get());
        map.put(IntSupplier.class, s -> ((IntSupplier)s).getAsInt());
        map.put(LongSupplier.class, s -> ((LongSupplier)s).getAsLong());
        map.put(BooleanSupplier.class, s -> ((BooleanSupplier)s).getAsBoolean());
        map.put(DoubleSupplier.class, s -> ((DoubleSupplier)s).getAsDouble());
        return Collections.unmodifiableMap(map);
    }

    ParameterProducer(FrameworkField field) {
        this.field = field;
    }

    Object produceParamValue() throws IllegalAccessException {
        return classToProducer.get(field.getType()).apply(field.get(null));
    }
}
