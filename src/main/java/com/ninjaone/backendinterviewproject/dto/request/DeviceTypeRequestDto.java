package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotEmpty;

public record DeviceTypeRequestDto(
    @NotEmpty(message = "name is required")
    String name){
}
