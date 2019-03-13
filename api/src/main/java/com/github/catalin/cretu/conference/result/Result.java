package com.github.catalin.cretu.conference.result;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public interface Result<T> {

    static <T> Result<T> error(final String code, final String message) {
        return new Error<>(code, message);
    }

    static <T> Result<T> errors(final Set<ErrorResult> errors) {
        return new Error<>(errors);
    }

    static <T> Ok<T> ok(final T result) {
        return new Ok<>(result);
    }

    /**
     * @return errors for Error Result or
     * @throws NoSuchElementException for OK Result
     */
    Set<ErrorResult> getErrors();

    boolean hasErrors();

    T get();

    class Error<T> implements Result<T> {

        private final Set<ErrorResult> errors = new HashSet<>();

        private Error(final String code, final String message) {
            errors.add(new ErrorResult(code, message));
        }

        private Error(final Set<ErrorResult> errs) {
            errors.addAll(errs);
        }


        @Override
        public Set<ErrorResult> getErrors() {
            return errors;
        }

        @Override
        public boolean hasErrors() {
            return true;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("Error result does not have an underlying object");
        }
    }

    class Ok<T> implements Result<T> {

        private final T result;

        private Ok(final T result) {
            this.result = result;
        }

        @Override
        public Set<ErrorResult> getErrors() {
            throw new NoSuchElementException("OK result does not have any errors");
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public T get() {
            return result;
        }
    }
}
