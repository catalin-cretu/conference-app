package com.github.catalin.cretu.conference.api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    private String title;
    private String description;
    private String location;

    private PeriodView period;

    private AuthorView author;
}