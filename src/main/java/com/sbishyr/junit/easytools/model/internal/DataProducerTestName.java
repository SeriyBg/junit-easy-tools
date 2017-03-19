package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.runners.model.FrameworkMethod;

/**
 * @author Serge Bishyr
 */
public class DataProducerTestName {

    private final FrameworkMethod method;

    public DataProducerTestName(FrameworkMethod method) {
        this.method = method;
    }

    public String name() {
        ProducedValues annotation = method.getAnnotation(ProducedValues.class);
        if (annotation != null && !annotation.name().isEmpty()) {
            return method.getName() + "[" + annotation.name() + "]";
        }
        return method.getName();
    }
}
