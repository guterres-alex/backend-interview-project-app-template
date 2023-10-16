package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.model.DeviceType;

public interface DeviceTypeService {

    DeviceType save(DeviceTypeRequestDto deviceTypeRequestDto);

    Iterable<DeviceType> findAll();

    DeviceType getById(Long id);

}
