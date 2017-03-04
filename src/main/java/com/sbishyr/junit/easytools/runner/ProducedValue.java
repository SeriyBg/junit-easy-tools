package com.sbishyr.junit.easytools.runner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Serge Bishyr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducedValue {
    String producer();
}
