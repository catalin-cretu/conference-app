package com.github.catalin.cretu.conference.api.event;

import com.github.catalin.cretu.conference.api.ErrorView;
import com.github.catalin.cretu.conference.api.ErrorsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNullElse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventsResponse extends ErrorsResponse {

    private Set<EventView> events;

    @Builder
    public EventsResponse(final Set<EventView> events, final List<ErrorView> errors) {
        super(requireNonNullElse(errors, emptyList()));
        this.events = requireNonNullElse(events, emptySet());
    }
}