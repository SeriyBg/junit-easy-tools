package com.sbishyr.junit.easytools.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by Serge Bishyr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducedValue {
    String producer();
}
