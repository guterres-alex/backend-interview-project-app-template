package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceServicesRepository extends CrudRepository<DeviceServicesEntity, Long> {

    Iterable<DeviceServicesEntity> findByDeviceId(Long deviceId);

    Iterable<DeviceServicesEntity> findByServicesId(Long serviceId);

}
