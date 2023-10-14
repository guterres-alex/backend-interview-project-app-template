package com.ninjaone.backendinterviewproject.dto.response;

import java.math.BigDecimal;

public record ServiceDetailResponseDto(Long id, String name, BigDecimal cost) {
}
