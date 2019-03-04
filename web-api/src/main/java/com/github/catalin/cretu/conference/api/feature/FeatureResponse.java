package com.github.catalin.cretu.conference.api.feature;

import com.github.catalin.cretu.conference.api.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeatureResponse extends ErrorResponse {

    @Getter
    @Setter
    private Set<FeatureView> features = new HashSet<>();
}
