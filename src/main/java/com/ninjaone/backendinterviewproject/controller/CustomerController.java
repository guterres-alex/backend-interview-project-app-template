package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesChangeRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerDeviceServicesResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerResponseDto;
import com.ninjaone.backendinterviewproject.exception.CustomerNotFoundException;
import com.ninjaone.backendinterviewproject.mapper.CustomerMapper;
import com.ninjaone.backendinterviewproject.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerOperations {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    public CustomerResponseDto postCustomerEntity(CustomerRequestDto customerRequestDto) {
        return ofNullable(service.saveCustomerEntity(customerRequestDto))
            .map(CustomerMapper.INSTANCE::customerToCustomerResponseDto)
            .orElseThrow();
    }

    public CustomerResponseDto getCustomerEntity(Long id) {
        return service.getCustomerEntity(id)
            .map(CustomerMapper.INSTANCE::customerToCustomerResponseDto)
            .orElseThrow(CustomerNotFoundException::new);
    }

    public void deleteCustomerEntity(Long id) {
        service.deleteCustomerEntity(id);
    }

    public CustomerDeviceServicesResponseDto postNewCustomerDeviceServices(
        Long customerId, List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList) {

        return ofNullable(service.assignNewDeviceServices(customerId, customerDeviceServicesRequestDtoList))
            .map(CustomerMapper.INSTANCE::customerToCustomerDeviceServicesDto)
            .orElseThrow();
    }

    public CustomerDeviceServicesResponseDto postCustomerDeviceServices(
        Long customerId, List<CustomerDeviceServicesChangeRequestDto> customerDeviceServicesChangeRequestDtoList) {

        return ofNullable(service.assignServicesExistentDeviceServices(customerId, customerDeviceServicesChangeRequestDtoList))
            .map(CustomerMapper.INSTANCE::customerToCustomerDeviceServicesDto)
            .orElseThrow();
    }

    public ResponseEntity<BigDecimal> getCustomerCalculations(Long customerId) {
        return ofNullable(service.getCalculations(customerId))
            .map(value -> ResponseEntity.ok().body(value))
            .orElseThrow();
    }

    public ResponseEntity<BigDecimal> getDynamicCalculations(Long customerId) {
        return ofNullable(service.getDynamicCalculations(customerId))
            .map(value -> ResponseEntity.ok().body(value))
            .orElseThrow();
    }

}
