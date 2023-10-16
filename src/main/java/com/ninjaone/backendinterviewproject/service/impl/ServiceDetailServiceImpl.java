package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.ServiceDetailRepository;
import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.exception.ServiceDetailException;
import com.ninjaone.backendinterviewproject.mapper.ServiceDetailMapper;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import com.ninjaone.backendinterviewproject.service.DeviceServicesService;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
import com.ninjaone.backendinterviewproject.service.ServiceDetailService;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceDetailServiceImpl implements ServiceDetailService {

    private final ServiceDetailRepository repository;
    private final DeviceTypeService deviceTypeService;
    private final DeviceServicesService deviceServicesService;
    private final MessageUtil messageUtil;

    public ServiceDetailServiceImpl(
        ServiceDetailRepository repository, DeviceTypeService deviceTypeService,
        DeviceServicesService deviceServicesService, MessageUtil messageUtil) {

        this.repository = repository;
        this.deviceTypeService = deviceTypeService;
        this.deviceServicesService = deviceServicesService;
        this.messageUtil = messageUtil;
    }

    public ServiceDetail saveServiceDetailEntity(ServiceDetailRequestDto serviceDto) {
        if (repository.findByNameIgnoreCaseAndDeviceTypeId(serviceDto.name(), serviceDto.deviceTypeId()).isPresent()) {
             throw new ServiceDetailException(messageUtil.getMessage("service.already.exists"));
        }

        DeviceType deviceType = deviceTypeService.getById(serviceDto.deviceTypeId());

        ServiceDetail service = ServiceDetailMapper.INSTANCE.serviceDetailRequestDtoToServiceDetail(serviceDto);
        service.setDeviceType(deviceType);

        return repository.save(service);
    }

    public Optional<ServiceDetail> findServiceDetailEntityById(Long id) {
        return repository.findById(id);
    }

    public Iterable<ServiceDetail> findAllServiceDetailEntity() {
        return repository.findAll();
    }

    public void deleteServiceDetailEntity(Long id) {
        Iterable<DeviceServicesEntity> deviceServices = deviceServicesService.findByServicesId(id);
        if (deviceServices.iterator().hasNext()) {
            throw new ServiceDetailException(messageUtil.getMessage(
                "service.id.used.for.customer",
                new Object[]{deviceServices.iterator().next().getDevice().getId()}));
        }
        repository.deleteById(id);
    }

    public List<ServiceDetail> getServicesByDeviceType(DeviceType type, List<Long> services) {
        List<ServiceDetail> serviceDetailList = new ArrayList<>();

        services.forEach(serviceId -> {
            ServiceDetail serviceDetail = getServiceDetail(serviceId);
            validateSameType(type, serviceDetail);
            serviceDetailList.add(serviceDetail);
        });

        return serviceDetailList;
    }

    private ServiceDetail getServiceDetail(Long serviceId) {
        return repository.findById(serviceId)
            .orElseThrow(() -> new ServiceDetailException(messageUtil.getMessage("service.not.exists")));
    }

    private void validateSameType(DeviceType type, ServiceDetail serviceDetail) {
        if (serviceDetail.getDeviceType() != null && !serviceDetail.getDeviceType().equals(type)) {
            throw new ServiceDetailException(messageUtil.getMessage(
                "service.type.is.different.from", new Object[]{type.getName()}));
        }
    }

    public Iterable<ServiceDetail> getAutomaticServices() {
        return repository.findByAutomatic(true);
    }
}
