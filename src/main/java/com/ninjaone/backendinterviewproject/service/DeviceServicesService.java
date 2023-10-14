package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;

public interface DeviceServicesService {

    DeviceServicesEntity save(DeviceServicesEntity deviceServicesEntity);

    Iterable<DeviceServicesEntity> findByDeviceId(Long id);

    Iterable<DeviceServicesEntity> findByServicesId(Long id);

}
