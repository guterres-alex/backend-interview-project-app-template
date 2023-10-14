package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.DeviceServicesRepository;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.service.DeviceServicesService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServicesServiceImpl implements DeviceServicesService {

    private final DeviceServicesRepository repository;

    public DeviceServicesServiceImpl(DeviceServicesRepository repository) {
        this.repository = repository;
    }

    public DeviceServicesEntity save(DeviceServicesEntity deviceServicesEntity) {
        return repository.save(deviceServicesEntity);
    }

    public Iterable<DeviceServicesEntity> findByDeviceId(Long id) {
        return repository.findByDeviceId(id);
    }

    public Iterable<DeviceServicesEntity> findByServicesId(Long id) {
        return repository.findByServicesId(id);
    }

}
