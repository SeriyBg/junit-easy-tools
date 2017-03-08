package com.sbishyr.junit.easytools.utils;

import org.assertj.core.api.Assertions;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom wrapper to provide quick assertions on {@link Result}
 * @author by Serge Bishyr
 */
public class ResultAssertions extends Assertions {

    /**
     * Checks that result has no failures.
     * Print all the failures in error message if there are any.
     */
    public static void assertResultHasNoFailures(Result result) {
        List<Failure> failures = result.getFailures();
        assertThat(failures)
                .as("No failures expected. Failures: %s",
                        failures.stream()
                                .map(ResultAssertions::formFailureDescription)
                                .collect(Collectors.toList()))
                .isEmpty();
    }

    private static String formFailureDescription(Failure failure) {
        return failure.getDescription() + " " + failure.getException().getMessage();
    }
}
