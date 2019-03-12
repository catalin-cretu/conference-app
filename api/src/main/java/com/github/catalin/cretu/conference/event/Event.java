package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Validatable;
import lombok.Builder;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

import static com.github.catalin.cretu.conference.result.ErrorResult.errorResult;
import static java.util.Objects.requireNonNullElseGet;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Value
@Builder
public class Event implements Validatable {

    private Long id;

    private String title;
    private String description;
    private String location;

    private Period period;

    private Author author;

    @Override
    public Set<ErrorResult> validate() {
        var errors = new HashSet<ErrorResult>();

        if (isBlank(title)) {
            errors.add(errorResult("title", "title must not be blank"));
        }
        if (isBlank(description)) {
            errors.add(errorResult("description", "description must not be blank"));
        }
        if (isBlank(location)) {
            errors.add(errorResult("location", "location must not be blank"));
        }
        var defaultPeriod = requireNonNullElseGet(getPeriod(), () -> Period.builder().build());
        errors.addAll(defaultPeriod.validate());

        var defaultAuthor = requireNonNullElseGet(getAuthor(), () -> Author.builder().build());
        errors.addAll(defaultAuthor.validate());

        return errors;
    }
}
