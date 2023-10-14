package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record DeviceRequestDto(
    @NotEmpty(message = "systemName is required")
    String systemName,
    @NotNull(message = "deviceTypeId is required")
    Long deviceTypeId){
}
