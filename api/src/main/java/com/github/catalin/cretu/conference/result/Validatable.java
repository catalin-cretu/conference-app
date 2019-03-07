package com.github.catalin.cretu.conference.result;

import java.util.Set;

public interface Validatable {

    Set<ErrorResult> validate();
}
