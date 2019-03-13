package com.github.catalin.cretu.conference.api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorView {

    private String name;
    private String jobTitle;
    private String companyName;
}