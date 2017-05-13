package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Serge Bishyr
 */
public class ProducerAssignmentsOneParameterTest {

    private ProducerAssignments assignments;

    public static class ClassWithAssignments {

        @DataProducer
        public static Supplier<String> supplier = () -> "42";

        public void a(String s) {
            //Do nothing
        }
    }

    @Before
    public void createAssignments() throws Exception {
        TestClass testClass = new TestClass(ClassWithAssignments.class);
        assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class));
    }

    @Test
    public void shouldBeIncompleteOnCreation() throws Exception {
        assertThat(assignments.isComplete()).isFalse();
    }

    @Test
    public void shouldGetNextPotentialParameterProducer() throws Throwable {
        List<ParameterProducer> parameterProducers = assignments.potentialNextParameterProducers();
        assertThat(parameterProducers).hasSize(1);
        assertThat(parameterProducers.get(0).produceParamValue()).isEqualTo("42");
    }

    @Test
    public void shouldGetNewAssignmentOnAssignNext() throws Throwable {
        List<ParameterProducer> parameterProducers = assignments.potentialNextParameterProducers();
        ProducerAssignments newAssignments = assignments.assignNext(parameterProducers.get(0));
        assertThat(newAssignments.isComplete()).isTrue();
        assertThat(newAssignments.getAssignedParams()).hasSize(1);
        assertThat(newAssignments.getAssignedParams().get(0).produceParamValue()).isEqualTo("42");
    }
}
