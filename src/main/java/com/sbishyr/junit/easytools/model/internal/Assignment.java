package com.sbishyr.junit.easytools.model.internal;

import org.junit.experimental.theories.ParameterSignature;

/**
 * @author Serge Bishyr
 */
interface Assignment {

    boolean isValidFor(ParameterSignature parameterSignature);

    ParameterProducer parameterProducer();
}
