package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesChangeRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.model.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer saveCustomerEntity(CustomerRequestDto customerRequestDto);

    Optional<Customer> getCustomerEntity(Long id);

    void deleteCustomerEntity(Long id);

    Customer assignNewDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList);

    Customer assignServicesExistentDeviceServices(
        Long customerId, List<CustomerDeviceServicesChangeRequestDto> customerDeviceServicesChangeRequestDtoList);

    BigDecimal getCalculations(Long customerId);

    BigDecimal getDynamicCalculations(Long customerId);
}
