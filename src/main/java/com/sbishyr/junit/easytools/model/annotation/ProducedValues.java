package com.sbishyr.junit.easytools.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Serge Bishyr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducedValues {
    int iterations() default 1;
}
