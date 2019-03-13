package com.github.catalin.cretu.conference.api.feature;

import com.github.catalin.cretu.conference.api.ErrorsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeaturesResponse extends ErrorsResponse {

    @Getter
    @Setter
    @Builder.Default
    private Set<FeatureView> features = new HashSet<>();
}
