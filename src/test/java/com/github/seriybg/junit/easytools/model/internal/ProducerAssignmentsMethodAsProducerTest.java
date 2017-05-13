package com.github.seriybg.junit.easytools.model.internal;

import com.github.seriybg.junit.easytools.model.annotation.DataProducer;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    public static class ProducerIsNotCollection {

        @DataProducer
        public static String dataProducer() {
            return "return";
        }

        public void a(String s) throws Exception {
            //Do nothing
        }
    }

    @Test
    public void shouldSupportMethodDataProducer() throws Throwable {
        TestClass testClass = new TestClass(ClassWithMethodProducer.class);
        ProducerAssignments assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class));

        List<ParameterProducer> parameterProducers = assignments.potentialNextParameterProducers();
        assertThat(parameterProducers).hasSize(1);
        assertThat(parameterProducers.get(0).produceParamValue()).isEqualTo("return");
    }

    @Test
    public void shouldValidateMethodProducerReturnType() throws Exception {
        TestClass testClass = new TestClass(ProducerIsNotCollection.class);
        ProducerAssignments assignments = ProducerAssignments.allUnassigned(
                testClass, testClass.getJavaClass().getMethod("a", String.class));

        assertThatExceptionOfType(InitializationError.class)
                .isThrownBy(assignments::potentialNextParameterProducers);
    }
}
