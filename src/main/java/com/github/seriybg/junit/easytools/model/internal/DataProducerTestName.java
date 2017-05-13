package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.runners.model.FrameworkMethod;

/**
 * @author Serge Bishyr
 */
class DataProducerTestName {

    private final FrameworkMethod method;

    private final Object[] params;

    private final int index;

    DataProducerTestName(FrameworkMethod method, Object[] params, int index) {
        this.method = method;
        this.params = params;
        this.index = index;
    }

    String name() {
        ProducedValues annotation = method.getAnnotation(ProducedValues.class);
        if (annotation != null && !annotation.name().isEmpty()) {
            String replacedName = annotation.name();
            replacedName = replacedName.replaceAll("\\{index}", String.valueOf(index));
            for (int i = 0; i < params.length; i++) {
                replacedName = replacedName.replaceAll("\\{" + i + "}", String.valueOf(params[i]));
            }
            return method.getName() + "[" + replacedName + "]";
        }
        return method.getName();
    }
}
