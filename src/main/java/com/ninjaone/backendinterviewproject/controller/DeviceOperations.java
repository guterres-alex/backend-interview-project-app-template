package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
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
@RequestMapping("/device")
public interface DeviceOperations {

    @Operation(summary = "Creates a new device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created device"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    DeviceResponseDto postDeviceEntity(@Valid @RequestBody DeviceRequestDto deviceRequestDto);

    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Device found"),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
        @ApiResponse(responseCode = "404", description = "Device not found")})
    @GetMapping("/{id}")
    DeviceResponseDto getDeviceEntity(@PathVariable Long id);

    @Operation(summary = "Delete a device by its id if its not bounded to any customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Device excluded"),
        @ApiResponse(responseCode = "500", description = "Server error")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDeviceEntity(@PathVariable Long id);

    @Operation(summary = "Get all device registered")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devices found"),
        @ApiResponse(responseCode = "404", description = "No devices were found")})
    @GetMapping
    List<DeviceResponseDto> getAllDeviceEntity();

}
