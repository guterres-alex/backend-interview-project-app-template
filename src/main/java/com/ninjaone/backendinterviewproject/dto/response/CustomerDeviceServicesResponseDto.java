package com.ninjaone.backendinterviewproject.dto.response;

import com.ninjaone.backendinterviewproject.model.DeviceType;

import java.math.BigDecimal;
import java.util.List;

public record CustomerDeviceServicesResponseDto(Long id, String name, List<DeviceServicesDto> deviceServiceEntities) {

    public record DeviceServicesDto(DeviceDto device, List<ServiceDetailDto> services) {
    }

    public record DeviceDto(String id, String systemName, DeviceType type) {
    }

    public record ServiceDetailDto(String id, String name, BigDecimal cost, DeviceType deviceType){
    }

}





