package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesChangeRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.exception.CustomerException;
import com.ninjaone.backendinterviewproject.mapper.CustomerMapper;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import com.ninjaone.backendinterviewproject.service.CustomerService;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import com.ninjaone.backendinterviewproject.service.DeviceServicesService;
import com.ninjaone.backendinterviewproject.service.ServiceDetailService;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final DeviceService deviceService;
    private final ServiceDetailService serviceDetailService;
    private final DeviceServicesService deviceServicesService;
    private final MessageUtil messageUtil;

    public CustomerServiceImpl(
        CustomerRepository repository, DeviceService deviceService, ServiceDetailService serviceDetailService,
        DeviceServicesService deviceServicesService, MessageUtil messageUtil) {

        this.repository = repository;
        this.deviceService = deviceService;
        this.serviceDetailService = serviceDetailService;
        this.deviceServicesService = deviceServicesService;
        this.messageUtil = messageUtil;
    }

    public Customer saveCustomerEntity(CustomerRequestDto customerRequestDto) {
        Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerRequestDto);
        return repository.save(customer);
    }

    public Optional<Customer> getCustomerEntity(Long id) {
        return repository.findById(id);
    }

    public void deleteCustomerEntity(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = {"totalCosts", "dynamicTotalCosts"}, key = "#customerId")
    public Customer assignNewDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList) {

        Customer customer = getCustomer(customerId);

        customerDeviceServicesRequestDtoList.forEach(customerDeviceServices -> {
            Device device = deviceService.getDevice(customerDeviceServices.deviceId());
            customer.getDeviceServiceEntities().add(createNewDeviceServices(customerDeviceServices, device));
        });

        return repository.save(customer);
    }

    private DeviceServicesEntity createNewDeviceServices(CustomerDeviceServicesRequestDto customerDeviceServices, Device device) {
        List<ServiceDetail> servicesByDeviceType = serviceDetailService.getServicesByDeviceType(device.getType(), customerDeviceServices.services());

        servicesByDeviceType.addAll(addAutomaticServices(device.getType()));

        return deviceServicesService.save(DeviceServicesEntity.builder()
            .device(device)
            .services(servicesByDeviceType)
            .totalCost(getTotalCost(servicesByDeviceType))
            .build());
    }

    @CacheEvict(value = {"totalCosts", "dynamicTotalCosts"}, key = "#customerId")
    public Customer assignServicesExistentDeviceServices(
        Long customerId, List<CustomerDeviceServicesChangeRequestDto> customerDeviceServicesChangeRequestDtoList) {

        Customer customer = getCustomer(customerId);

        customerDeviceServicesChangeRequestDtoList.forEach(requestDto -> {
            DeviceServicesEntity deviceServices = customer.getDeviceServiceEntities().stream()
                .filter(customerDeviceServices -> customerDeviceServices.getId().equals(requestDto.deviceServiceId()))
                .findFirst()
                .orElseThrow(() -> new CustomerException(messageUtil.getMessage(
                    "service.device.not.exists", new Object[]{requestDto.deviceServiceId()})));

            List<ServiceDetail> servicesByDeviceType = serviceDetailService.getServicesByDeviceType(
                deviceServices.getDevice().getType(), requestDto.services());

            servicesByDeviceType.addAll(addAutomaticServices(deviceServices.getDevice().getType()));

            if (!servicesByDeviceType.isEmpty()) {
                deviceServices.setServices(servicesByDeviceType);
                deviceServices.setTotalCost(getTotalCost(servicesByDeviceType));
            }
        });

        return repository.save(customer);
    }

    private List<ServiceDetail> addAutomaticServices(DeviceType type) {
        List<ServiceDetail> serviceDetailList = new ArrayList<>();
        serviceDetailService.getAutomaticServices().forEach(serviceDetail -> {
            if (serviceDetail.getDeviceType() == null || serviceDetail.getDeviceType().equals(type)) {
                serviceDetailList.add(serviceDetail);
            }
        });

        return serviceDetailList;
    }

    private BigDecimal getTotalCost(List<ServiceDetail> servicesByDeviceType) {
        return servicesByDeviceType.stream()
            .map(ServiceDetail::getCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Customer getCustomer(Long customerId) {
        return repository.findById(customerId)
            .orElseThrow(() -> new CustomerException(
                messageUtil.getMessage("customer.not.exists")));
    }

    @Cacheable("totalCosts")
    public BigDecimal getCalculations(Long customerId) {
        Customer customer = getCustomer(customerId);

        return customer.getDeviceServiceEntities().stream()
            .map(DeviceServicesEntity::getTotalCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Cacheable("dynamicTotalCosts")
    public BigDecimal getDynamicCalculations(Long customerId) {
        return getCustomer(customerId).getDeviceServiceEntities().stream()
            .map(DeviceServicesEntity::getServices)
            .flatMap(serviceDetails -> serviceDetails.stream()
                .map(ServiceDetail::getCost))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
