package com.sbishyr.junit.easytools.model.internal;

import com.sbishyr.junit.easytools.model.annotation.DataProducer;
import com.sbishyr.junit.easytools.model.annotation.ProducedValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class ProducerAssignmentsMultipleParametersTest {

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

    public static class ClassWithNamedProducer {

        @DataProducer(name = "notThis")
        public static Supplier<String> notThis = () -> "42";

        @DataProducer(name = "thisOne")
        public static Supplier<String> thisOne = () -> "27";

        public void a(@ProducedValue(producer = "thisOne") String s) {
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

    @Test
    public void shouldAssignNamedProducer() throws Exception {
        TestClass testClass = new TestClass(ClassWithNamedProducer.class);
        assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class));

        List<ParameterProducer> stringParameterProducer = assignments.potentialNextParameterProducers();
        assertThat(stringParameterProducer).hasSize(1);
        assertThat(stringParameterProducer.get(0).produceParamValue()).isEqualTo("27");

        assertThat(assignments.assignNext(stringParameterProducer.get(0)).isComplete()).isTrue();
    }
}