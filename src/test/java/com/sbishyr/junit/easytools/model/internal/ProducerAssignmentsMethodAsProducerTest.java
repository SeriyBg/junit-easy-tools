package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class ProducerAssignmentsMethodAsProducerTest {

    public static class ClassWithMethodProducer {

        @DataProducer
        public static List<Supplier<String>> dataProducer() {
            return Arrays.asList(
                    () -> "return"
            );
        }

        public void a(String s) {
            //Do nothing
        }
    }

    @Test
    @Ignore("#29")
    public void shouldSupportMethodDataProducer() throws Exception {
        TestClass testClass = new TestClass(ClassWithMethodProducer.class);
        ProducerAssignments assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class));

        List<ParameterProducer> parameterProducers = assignments.potentialNextParameterProducers();
        assertThat(parameterProducers).hasSize(1);
        assertThat(parameterProducers.get(0).produceParamValue()).isEqualTo("return");
    }
}
