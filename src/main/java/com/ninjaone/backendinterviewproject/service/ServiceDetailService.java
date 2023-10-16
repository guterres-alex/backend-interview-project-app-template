package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;

import java.util.List;
import java.util.Optional;

public interface ServiceDetailService {

    ServiceDetail saveServiceDetailEntity(ServiceDetailRequestDto serviceDto);

    Optional<ServiceDetail> findServiceDetailEntityById(Long id);

    void deleteServiceDetailEntity(Long id);

    List<ServiceDetail> getServicesByDeviceType(DeviceType type, List<Long> services);

    Iterable<ServiceDetail> findAllServiceDetailEntity();

    Iterable<ServiceDetail> getAutomaticServices();
}
