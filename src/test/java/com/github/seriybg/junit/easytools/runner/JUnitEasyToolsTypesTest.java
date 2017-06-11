package com.github.seriybg.junit.easytools.runner;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

/**
 * @author Serge Bishyr
 */
@RunWith(JUnitEasyTools.class)
public class JUnitEasyToolsTypesTest {

    @DataProducer
    public static Supplier<Integer> intsSupplier = () -> 227;

    @Test
    public void shouldSupplyIntsFromInteger(int value) throws Exception {
        assertEquals(value, 227);
    }
}