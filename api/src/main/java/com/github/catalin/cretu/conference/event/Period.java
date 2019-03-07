package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Validatable;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.github.catalin.cretu.conference.result.ErrorResult.error;

@Value
@Builder
public class Period implements Validatable {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Override
    public Set<ErrorResult> validate() {
        var errors = new HashSet<ErrorResult>();

        if (startDateTime == null) {
            errors.add(error("startDateTime", "startDateTime must not be blank"));
        }
        if (endDateTime == null) {
            errors.add(error("endDateTime", "endDateTime must not be blank"));
        }
        return errors;
    }
}
