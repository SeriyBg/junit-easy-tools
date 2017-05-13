package com.github.seriybg.junit.easytools.model.internal;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * @author Serge Bishyr
 */
class ParameterProducer {


    private final Type type;
    private final Object dataProducer;

    private static final Map<Type, Function<Object, Object>> classToProducer = createClassToProducer();

    private static Map<Type, Function<Object, Object>> createClassToProducer() {
        Map<Type, Function<Object, Object>> map = new HashMap<>();
        map.put(Supplier.class, s -> ((Supplier<?>)s).get());
        map.put(IntSupplier.class, s -> ((IntSupplier)s).getAsInt());
        map.put(LongSupplier.class, s -> ((LongSupplier)s).getAsLong());
        map.put(BooleanSupplier.class, s -> ((BooleanSupplier)s).getAsBoolean());
        map.put(DoubleSupplier.class, s -> ((DoubleSupplier)s).getAsDouble());
        return Collections.unmodifiableMap(map);
    }

    ParameterProducer(Type type, Object dataProducer) {
        this.type = type;
        this.dataProducer = dataProducer;
    }

    Object produceParamValue() {
        return classToProducer.get(type).apply(dataProducer);
    }
}
