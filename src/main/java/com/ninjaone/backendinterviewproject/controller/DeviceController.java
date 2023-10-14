package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.exception.DeviceNotFoundException;
import com.ninjaone.backendinterviewproject.mapper.DeviceMapper;
import com.ninjaone.backendinterviewproject.mapper.DeviceTypeMapper;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/device")
public class DeviceController implements DeviceOperations {

    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    public DeviceResponseDto postDeviceEntity(DeviceRequestDto deviceRequestDto) {
        return ofNullable(service.saveDeviceEntity(deviceRequestDto))
            .map(DeviceMapper.INSTANCE::deviceToDeviceResponseDto)
            .orElseThrow();
    }

    public DeviceResponseDto getDeviceEntity(Long id) {
        return service.getDeviceEntity(id)
            .map(DeviceMapper.INSTANCE::deviceToDeviceResponseDto)
            .orElseThrow(DeviceNotFoundException::new);
    }

    public void deleteDeviceEntity(Long id) {
        service.deleteDeviceEntity(id);
    }

    public List<DeviceResponseDto> getAllDeviceEntity() {
        return ofNullable(service.getAllDeviceEntity())
            .map(DeviceMapper.INSTANCE::deviceListToDeviceResponseDtoList)
            .orElseThrow(DeviceNotFoundException::new);
    }

    public DeviceTypeResponseDto postDeviceTypeEntity(DeviceTypeRequestDto deviceTypeRequestDto) {
        return ofNullable(service.saveDeviceTypeEntity(deviceTypeRequestDto))
            .map(DeviceTypeMapper.INSTANCE::deviceTypeToDeviceTypeResponseDto)
            .orElseThrow();
    }

    public List<DeviceTypeResponseDto> getAllDeviceTypeEntity() {
        return ofNullable(service.getAllDeviceType())
            .map(DeviceTypeMapper.INSTANCE::deviceListToDeviceResponseDtoList)
            .orElseThrow(DeviceNotFoundException::new);
    }

}
