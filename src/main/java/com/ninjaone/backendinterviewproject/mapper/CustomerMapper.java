package com.ninjaone.backendinterviewproject.mapper;

import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerDeviceServicesResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerResponseDto;
import com.ninjaone.backendinterviewproject.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerDtoToCustomer(CustomerRequestDto customerRequestDto);
    CustomerResponseDto customerToCustomerResponseDto(Customer customer);
    CustomerDeviceServicesResponseDto customerToCustomerDeviceServicesDto(Customer customer);

}
