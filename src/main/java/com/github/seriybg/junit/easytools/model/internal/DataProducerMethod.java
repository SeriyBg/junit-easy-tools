package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Serge Bishyr
 */
class DataProducerMethod {

    private final FrameworkMethod method;
    private final Object test;
    private final Object[] params;

    DataProducerMethod(FrameworkMethod method, Object test, Object[] params) {
        this.method = method;
        this.test = test;
        this.params = params;
        validateParams();
    }

    void invoke() throws Throwable {
        method.invokeExplosively(test, params);
    }

    private void validateParams() {
        final ProducedValues marker = method.getAnnotation(ProducedValues.class);
        if (marker != null
                && !marker.nullsAccepted() && Arrays.stream(params).anyMatch(Objects::isNull)) {
            throw new AssumptionViolatedException(
                    "Nulls not allowed as parameter for test method: " + method.getName());
        }
    }
}
