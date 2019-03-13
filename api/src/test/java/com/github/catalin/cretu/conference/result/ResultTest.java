package com.github.catalin.cretu.conference.result;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

class ResultTest {

    @Test
    @DisplayName("error - Returns Error Result")
    void error() {
        assertThat(Result.error("c", "err"))
                .isInstanceOf(Result.Error.class)
                .extracting(Result::hasErrors)
                .isEqualTo(true);

        Result<Object> errors = Result.errors(Set.of(
                new ErrorResult("a", "e1"),
                new ErrorResult("b", "e2")));
        assertThat(errors.getErrors())
                .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                .containsSequence(
                        tuple("a", "e1"),
                        tuple("b", "e2"));
    }

    @Test
    @DisplayName("getErrors - With Okay Result  - Throws Exception")
    void getErrors_WhenOK() {
        assertThatThrownBy(() -> Result.ok(ONE).getErrors())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("OK")
                .hasMessageContaining("does not have any errors");
    }

    @Test
    @DisplayName("ok - Returns Result")
    void ok() {
        assertThat(Result.ok(101L))
                .isInstanceOf(Result.Ok.class)
                .extracting(Result::get, Result::hasErrors)
                .containsSequence(101L, false);
    }

    @Test
    @DisplayName("get - With Errors - Throws Exception")
    void get_WithErrors() {
        assertThatThrownBy(() -> Result.error("a", "b").get())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Error")
                .hasMessageContaining("does not have an underlying object");
    }
}