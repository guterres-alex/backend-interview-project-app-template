package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;

import java.util.Optional;

public interface DeviceService {

    Device saveDeviceEntity(DeviceRequestDto deviceRequestDto);

    Optional<Device> getDeviceEntity(Long id);

    Iterable<Device> getAllDeviceEntity();

    void deleteDeviceEntity(Long id);

    Device getDevice(Long deviceId);

}
