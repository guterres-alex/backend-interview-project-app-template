package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record CustomerDeviceServicesRequestDto(
    @NotNull Long deviceId,
    @NotEmpty List<Long> services,
    @NotNull Integer quantity) {
}
