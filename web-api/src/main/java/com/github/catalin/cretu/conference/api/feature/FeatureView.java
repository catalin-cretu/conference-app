package com.github.catalin.cretu.conference.api.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureView {

    private String name;
    private boolean active;
}
