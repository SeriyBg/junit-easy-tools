package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.ProducedValues;
import org.junit.runners.model.FrameworkMethod;

/**
 * @author Serge Bishyr
 */
class DataProducerTestName {

    private final FrameworkMethod method;

    private final Object[] params;

    DataProducerTestName(FrameworkMethod method, Object[] params) {
        this.method = method;
        this.params = params;
    }

    String name() {
        ProducedValues annotation = method.getAnnotation(ProducedValues.class);
        if (annotation != null && !annotation.name().isEmpty()) {
            String replacedName = annotation.name();
            for (int i = 0; i < params.length; i++) {
                replacedName = replacedName.replaceAll("\\{" + i + "}", String.valueOf(params[i]));
            }
            return method.getName() + "[" + replacedName + "]";
        }
        return method.getName();
    }
}
