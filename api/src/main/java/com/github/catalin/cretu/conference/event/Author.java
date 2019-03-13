package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Validatable;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import static com.github.catalin.cretu.conference.result.ErrorResult.errorResult;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Data
@Builder
public class Author implements Validatable {

    private String name;
    private String jobTitle;
    private String companyName;

    @Override
    public Set<ErrorResult> validate() {
        var errors = new HashSet<ErrorResult>();

        if (isBlank(name)) {
            errors.add(errorResult("name", "author name must not be blank"));
        }
        if (isBlank(jobTitle)) {
            errors.add(errorResult("jobTitle", "author job title must not be blank"));
        }
        if (isBlank(companyName)) {
            errors.add(errorResult("companyName", "author company name must not be blank"));
        }
        return errors;
    }
}
