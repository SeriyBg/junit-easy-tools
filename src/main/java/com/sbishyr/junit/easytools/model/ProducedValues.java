package com.sbishyr.junit.easytools.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Serge Bishyr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducedValues {
    int iterations() default 1;
}
