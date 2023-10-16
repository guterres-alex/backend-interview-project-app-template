package com.ninjaone.backendinterviewproject.dto.response;

import com.ninjaone.backendinterviewproject.model.DeviceType;

import java.math.BigDecimal;
import java.util.List;

public record CustomerDeviceServicesResponseDto(Long id, String name, List<DeviceServicesDto> deviceServiceEntities) {

    public record DeviceServicesDto(Long id, DeviceDto device, List<ServiceDetailDto> services) {
    }

    public record DeviceDto(Long id, String systemName, DeviceType type) {
    }

    public record ServiceDetailDto(Long id, String name, BigDecimal cost, DeviceType deviceType){
    }

}





