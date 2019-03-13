package com.github.catalin.cretu.conference.api.event;

import com.github.catalin.cretu.conference.api.ErrorView;
import com.github.catalin.cretu.conference.api.ErrorsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNullElse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateEventResponse extends ErrorsResponse {

    private Long id;

    @Builder
    public CreateEventResponse(final Long id, final List<ErrorView> errors) {
        super(requireNonNullElse(errors, emptyList()));
        this.id = id;
    }
}