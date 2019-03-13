package com.github.catalin.cretu.conference.api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventView {

    private Long id;

    private PeriodView period;

    private String title;
    private String description;
    private String location;

    private AuthorView author;
}
