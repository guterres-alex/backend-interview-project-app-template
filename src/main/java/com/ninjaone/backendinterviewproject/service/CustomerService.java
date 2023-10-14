package com.ninjaone.backendinterviewproject.service;

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

    Customer assignDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList);

    BigDecimal getCalculations(Long customerId);

    Customer deleteDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList);

    BigDecimal getDynamicCalculations(Long customerId);
}
