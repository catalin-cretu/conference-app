package com.github.catalin.cretu.conference.result;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResult {

    private final String code;
    private final String message;

    public static ErrorResult errorResult(final String code, final String message) {
        return new ErrorResult(code, message);
    }
}
