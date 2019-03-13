package com.github.catalin.cretu.conference.api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodView {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
