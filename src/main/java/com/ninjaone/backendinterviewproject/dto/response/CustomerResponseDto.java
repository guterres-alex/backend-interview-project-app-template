package com.ninjaone.backendinterviewproject.dto.response;

import java.util.List;

public record CustomerResponseDto(
    Long id, String name, List<DeviceResponseDto> devices, List<ServiceDetailResponseDto> services) {
}
