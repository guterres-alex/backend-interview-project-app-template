package com.ninjaone.backendinterviewproject.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ServiceDetailRequestDto(
    @NotEmpty String name,
    @NotNull BigDecimal cost,
    Long deviceTypeId) {
}
