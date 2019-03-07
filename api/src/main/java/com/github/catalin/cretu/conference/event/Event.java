package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Validatable;
import lombok.Builder;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

import static com.github.catalin.cretu.conference.result.ErrorResult.error;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Value
@Builder
public class Event implements Validatable {

    private Long id;

    private Period period;

    private String title;
    private String description;
    private String location;

    private Author author;

    @Override
    public Set<ErrorResult> validate() {
        var errors = new HashSet<ErrorResult>();

        if (isBlank(title)) {
            errors.add(error("title", "title must not be blank"));
        }
        if (isBlank(description)) {
            errors.add(error("description", "description must not be blank"));
        }
        if (isBlank(location)) {
            errors.add(error("location", "location must not be blank"));
        }
        errors.addAll(author.validate());
        errors.addAll(period.validate());

        return errors;
    }
}
