package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.exception.DeviceNotFoundException;
import com.ninjaone.backendinterviewproject.mapper.DeviceTypeMapper;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/device/type")
public class DeviceTypeController implements DeviceTypeOperations {

    private final DeviceTypeService service;

    public DeviceTypeController(DeviceTypeService service) {
        this.service = service;
    }

    public DeviceTypeResponseDto postDeviceTypeEntity(DeviceTypeRequestDto deviceTypeRequestDto) {
        return ofNullable(service.save(deviceTypeRequestDto))
            .map(DeviceTypeMapper.INSTANCE::deviceTypeToDeviceTypeResponseDto)
            .orElseThrow();
    }

    public List<DeviceTypeResponseDto> getAllDeviceTypeEntity() {
        return ofNullable(service.findAll())
            .map(DeviceTypeMapper.INSTANCE::deviceListToDeviceResponseDtoList)
            .orElseThrow(DeviceNotFoundException::new);
    }

}
