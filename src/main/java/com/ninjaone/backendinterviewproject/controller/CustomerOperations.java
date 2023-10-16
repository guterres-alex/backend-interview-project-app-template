package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesChangeRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerDeviceServicesResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customer")
public interface CustomerOperations {

    @Operation(summary = "Creates a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created customer"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto postCustomerEntity(@Valid @RequestBody CustomerRequestDto customerRequestDto);

    @Operation(summary = "Get a customer by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the customer"),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")})
    @GetMapping("/{id}")
    CustomerResponseDto getCustomerEntity(@PathVariable Long id);

    @Operation(summary = "Delete a customer by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer excluded"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomerEntity(@PathVariable Long id);

    @Operation(summary = "Assigns to a customer new devices by its id and services chosen by a list of ids")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Device and services assigned successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @PostMapping("/{customer-id}/assign")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerDeviceServicesResponseDto postNewCustomerDeviceServices(
        @PathVariable("customer-id") Long customerId,
        @Valid @RequestBody List<CustomerDeviceServicesRequestDto> customerDeviceServicesRequestDtoList);

    @Operation(summary = "Assigns to a customer a device by its id and services chosen by a list of ids")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Device and services assigned successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @PostMapping("/{customer-id}/assign/device-service")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerDeviceServicesResponseDto postCustomerDeviceServices(
        @PathVariable("customer-id") Long customerId,
        @Valid @RequestBody List<CustomerDeviceServicesChangeRequestDto> customerDeviceServicesChangeRequestDtoList);

    @Operation(summary = "Calculate the total cost of services and devices assigned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calculation done successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping("/{customer-id}/calculate")
    ResponseEntity<BigDecimal> getCustomerCalculations(
        @PathVariable("customer-id") Long customerId);

    @Operation(summary = "Calculate dynamic the total cost of services and devices assigned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calculation done successfully"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping("/{customer-id}/calculate/dynamic")
    ResponseEntity<BigDecimal> getDynamicCalculations(
        @PathVariable("customer-id") Long customerId);

}
