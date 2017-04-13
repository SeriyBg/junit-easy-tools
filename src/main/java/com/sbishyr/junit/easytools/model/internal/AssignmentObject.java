package com.sbishyr.junit.easytools.model.internal;

import org.junit.experimental.theories.ParameterSignature;

/**
 * @author Serge Bishyr
 */
interface AssignmentObject {

    boolean isValidFor(ParameterSignature parameterSignature);

    ParameterProducer parameterProducer();
}
