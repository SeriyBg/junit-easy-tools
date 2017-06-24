package com.github.seriybg.junit.easytools.runner;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import com.github.seriybg.junit.easytools.model.annotation.ProducedValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
@RunWith(JUnitEasyTools.class)
public class JUnitEasyToolsNullValuesTest {

    private static final Queue<String> valuesWithNulls = new LinkedList<>();

    @DataProducer
    public static Supplier<String> producerWithNulls = valuesWithNulls::poll;

    @Before
    public void fillValues() throws Exception {
        valuesWithNulls.add("firstString");
        valuesWithNulls.add(null);
        valuesWithNulls.add("thirdOne");
        valuesWithNulls.add(null);
    }

    @Test
    @ProducedValues(nullsAccepted = false, iterations = 4)
    public void shouldNotAcceptNulls(String value) throws Exception {
        assertThat(value).isNotNull();
    }
}
