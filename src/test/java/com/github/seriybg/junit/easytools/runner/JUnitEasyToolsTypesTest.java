package com.github.seriybg.junit.easytools.runner;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Serge Bishyr
 */
@RunWith(JUnitEasyTools.class)
public class JUnitEasyToolsTypesTest {

    @DataProducer
    public static Supplier<Integer> intsSupplier = () -> 227;

    @DataProducer
    public static Supplier<Long> longSupplier = () -> 1234L;

    @DataProducer
    public static Supplier<Byte> byteSupplier = () -> (byte) 27;

    @DataProducer
    public static Supplier<Boolean> booleanSupplier = () -> false;

    @DataProducer
    public static Supplier<Short> shortSupplier = () -> (short) 15;

    @DataProducer
    public static Supplier<Character> characterSupplier = () -> 's';

    @DataProducer
    public static Supplier<Double> doubleSupplier = () -> 2.7;

    @DataProducer
    public static Supplier<Float> floatSupplier = () -> 2.7f;

    @Test
    public void shouldSupplyIntsFromInteger(int value) throws Exception {
        assertEquals(value, 227);
    }

    @Test
    public void shouldSupplyLongTypeFromLong(long value) throws Exception {
        assertEquals(value, 1234L);
    }

    @Test
    public void shouldSupplyByteTypeFromByte(byte value) throws Exception {
        assertEquals(value, (byte) 27);
    }

    @Test
    public void shouldSupplyBoolTypeFromBoolean(boolean value) throws Exception {
        assertFalse(value);
    }

    @Test
    public void shouldSupplyShortTypeFromShort(short value) throws Exception {
        assertEquals(value, (short) 15);
    }

    @Test
    public void shouldSupplyCharTypeFromCharacter(char value) throws Exception {
        assertEquals(value, 's');
    }

    @Test
    public void shouldSupplyFloatTypeFromFloat(float value) throws Exception {
        assertEquals(value, 2.7f, 2.7);
    }
}