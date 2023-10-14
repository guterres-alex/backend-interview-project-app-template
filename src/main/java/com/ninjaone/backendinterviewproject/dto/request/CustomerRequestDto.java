package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotEmpty;

public record CustomerRequestDto(
    @NotEmpty(message = "name must not be empty")
    String name) {
}
