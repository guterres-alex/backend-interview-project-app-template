package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public record CustomerDeviceServicesChangeRequestDto(
    @NotNull Long deviceServiceId,
    @NotNull List<Long> services) {
}
