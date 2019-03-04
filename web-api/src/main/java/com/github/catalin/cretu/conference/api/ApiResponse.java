package com.github.catalin.cretu.conference.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse<T> extends ResponseEntity<T> {

    private ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static <T> ApiResponse<T> ok(final T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ApiResponse<>(body, httpHeaders, HttpStatus.OK);
    }
}
