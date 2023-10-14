package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.ServiceDetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/service")
public interface ServiceDetailOperations {

    @Operation(summary = "Creates a new service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created service"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ServiceDetailResponseDto postServiceDetailEntity(@Valid @RequestBody ServiceDetailRequestDto serviceDto);

    @Operation(summary = "Get a service by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service found"),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
        @ApiResponse(responseCode = "404", description = "Service not found")})
    @GetMapping("/{id}")
    ServiceDetailResponseDto getServiceDetailEntity(@PathVariable Long id);

    @Operation(summary = "Get all services registered")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Services found"),
        @ApiResponse(responseCode = "404", description = "No services were found")})
    @GetMapping
    List<ServiceDetailResponseDto> getAllServiceDetailEntity();

    @Operation(summary = "Delete a service by its id if its not bounded to any customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Service excluded"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteServiceDetailEntity(@PathVariable Long id);

}
