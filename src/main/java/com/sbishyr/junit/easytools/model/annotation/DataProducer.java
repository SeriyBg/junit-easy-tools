package com.sbishyr.junit.easytools.model.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Serge Bishyr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataProducer {

    String name() default "";
}
