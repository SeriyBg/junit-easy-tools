package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author by Serge Bishyr
 */
public class MultipleParametersProducerAssignmentsTest {

    private ProducerAssignments assignments;

    public static class ClassWithMultipleAssignments {

        @DataProducer
        public static Supplier<String> stringSupplier = () -> "42";

        @DataProducer
        public static Supplier<Integer> integerSupplier = () -> 27;

        public void a(String s, Integer i) {
            //Do nothing
        }
    }

    @Before
    public void createAssignments() throws Exception {
        TestClass testClass = new TestClass(ClassWithMultipleAssignments.class);
        assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class, Integer.class));
    }

    @Test
    public void shouldGetPotentialForNextOfTheGivenType() throws Exception {
        List<ParameterProducer> stringParameterProducer = assignments.potentialNextParameterProducers();
        assertThat(stringParameterProducer).hasSize(1);
        assertThat(stringParameterProducer.get(0).produceParamValue()).isEqualTo("42");

        ProducerAssignments nextAssignments = assignments.assignNext(stringParameterProducer.get(0));
        assertThat(assignments.isComplete()).isFalse();

        List<ParameterProducer> integerParameterProducers = nextAssignments.potentialNextParameterProducers();
        assertThat(integerParameterProducers).hasSize(1);
        assertThat(integerParameterProducers.get(0).produceParamValue()).isEqualTo(27);
        ProducerAssignments assigned = nextAssignments.assignNext(integerParameterProducers.get(0));

        assertThat(assigned.isComplete()).isTrue();
    }
}