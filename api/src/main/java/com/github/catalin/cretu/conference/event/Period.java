package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Validatable;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.github.catalin.cretu.conference.result.ErrorResult.errorResult;

@Value
@Builder
public class Period implements Validatable {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Override
    public Set<ErrorResult> validate() {
        var errors = new HashSet<ErrorResult>();

        if (startDateTime == null) {
            errors.add(errorResult("startDateTime", "period start date and time must not be blank"));
        }
        if (endDateTime == null) {
            errors.add(errorResult("endDateTime", "period end date and time must not be blank"));
        } else if (startDateTime != null
                && startDateTime.isAfter(endDateTime)) {
            errors.add(errorResult("startDateTime", "period start date and time must be after end date and time"));
        }
        return errors;
    }
}