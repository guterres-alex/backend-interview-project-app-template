package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/device/type")
public interface DeviceTypeOperations {

    @Operation(summary = "Creates a new device type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created device type"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    DeviceTypeResponseDto postDeviceTypeEntity(@Valid @RequestBody DeviceTypeRequestDto deviceTypeRequestDto);

    @Operation(summary = "Get all device types registered")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Types of device found"),
        @ApiResponse(responseCode = "404", description = "No type of device were found")})
    @GetMapping
    List<DeviceTypeResponseDto> getAllDeviceTypeEntity();

}
