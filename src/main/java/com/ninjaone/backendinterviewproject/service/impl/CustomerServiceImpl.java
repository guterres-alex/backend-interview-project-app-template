package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public Customer assignDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList) {

        Customer customer = getCustomer(customerId);

        customerDeviceServicesRequestDtoList.forEach(customerDeviceServices -> {

            Device device = deviceService.getDevice(customerDeviceServices.deviceId());

            Optional<DeviceServicesEntity> deviceServices = getDeviceServicesFromDevice(customer, device);

            if (shouldAddToExistentDeviceServices(deviceServices)) {
                List<ServiceDetail> servicesByDeviceType =
                    serviceDetailService.getServicesByDeviceType(device.getType(), customerDeviceServices.services());

                if (!servicesByDeviceType.isEmpty()) {
                    deviceServices.get().getServices().addAll(servicesByDeviceType);
                    deviceServices.get().setTotalCost(deviceServices.get().getTotalCost().add(getTotalCost(servicesByDeviceType, deviceServices.get().getQuantity())));
                }
            } else {
                customer.getDeviceServiceEntities().add(createNewDeviceServices(customerDeviceServices, device));
            }
        });

        return repository.save(customer);
    }

    private boolean shouldAddToExistentDeviceServices(Optional<DeviceServicesEntity> deviceServices) {
        return deviceServices.isPresent();
    }

    private DeviceServicesEntity createNewDeviceServices(CustomerDeviceServicesRequestDto customerDeviceServices, Device device) {
        List<ServiceDetail> servicesByDeviceType = serviceDetailService.getServicesByDeviceType(device.getType(), customerDeviceServices.services());

        servicesByDeviceType.addAll(addAutomaticServices(device.getType()));

        return deviceServicesService.save(DeviceServicesEntity.builder()
            .device(device)
            .services(servicesByDeviceType)
            .totalCost(getTotalCost(servicesByDeviceType, customerDeviceServices.quantity()))
            .quantity(customerDeviceServices.quantity())
            .build());
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

    private BigDecimal getTotalCost(List<ServiceDetail> servicesByDeviceType, Integer quantity) {
        return servicesByDeviceType.stream()
            .map(serviceDetail -> getDeviceCost(quantity, serviceDetail))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getDeviceCost(Integer quantity, ServiceDetail serviceDetail) {
        if (serviceDetail.isAutomatic()) {
            return serviceDetail.getCost().multiply(new BigDecimal(quantity));
        }

        return serviceDetail.getCost();
    }

    private Optional<DeviceServicesEntity> getDeviceServicesFromDevice(Customer customer, Device device) {
        if (customer.getDeviceServiceEntities() != null) {
            for (DeviceServicesEntity deviceServicesEntity : customer.getDeviceServiceEntities()) {
                if (deviceServicesEntity.getDevice().equals(device)) {
                    return Optional.of(deviceServicesEntity);
                }
            }
        }

        return Optional.empty();
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

    @CacheEvict(value = {"totalCosts", "dynamicTotalCosts"}, key = "#customerId")
    public Customer deleteDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList) {

        Customer customer = getCustomer(customerId);

        customerDeviceServicesRequestDtoList.forEach(customerDeviceServices -> {

            Device device = deviceService.getDevice(customerDeviceServices.deviceId());

            Optional<DeviceServicesEntity> deviceServicesByDevice =
                getDeviceServicesFromDevice(customer, device);

            deviceServicesByDevice.ifPresent(
                deviceServices -> {
                    List<ServiceDetail> servicesByDeviceType = serviceDetailService.getServices(customerDeviceServices.services());

                    if (!servicesByDeviceType.isEmpty()) {
                        deviceServices.getServices().removeAll(servicesByDeviceType);
                        deviceServices.setTotalCost(deviceServices.getTotalCost().subtract(getTotalCost(servicesByDeviceType, deviceServices.getQuantity())));
                    }
                });
        });

        return repository.save(customer);
    }

    @Cacheable("dynamicTotalCosts")
    public BigDecimal getDynamicCalculations(Long customerId) {
        return getCustomer(customerId).getDeviceServiceEntities().stream()
            .map(deviceServicesEntity -> new ImmutablePair<>(deviceServicesEntity.getServices(), deviceServicesEntity.getQuantity()))
            .map(pair -> Stream.concat(
                pair.left.stream()
                    .filter(ServiceDetail::isAutomatic)
                    .map(serviceDetail -> serviceDetail.getCost().multiply(BigDecimal.valueOf(pair.right))),
                pair.left.stream()
                    .filter(serviceDetail -> !serviceDetail.isAutomatic())
                    .map(ServiceDetail::getCost))
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
