package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class})
@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class DeviceOperationsTest {

    public static final Long DEVICE_ID = 12345L;
    public static final Long DEVICE_TYPE_ID = 54321L;
    public static final String DEVICE_TYPE_NAME = "TestDeviceType";
    public static final String DEVICE_NAME = "TestDevice";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceService service;

    private DeviceTypeRequestDto deviceTypeRequestDto;

    private DeviceTypeResponseDto deviceTypeResponseDto;

    private DeviceRequestDto deviceRequestDto;

    private DeviceResponseDto deviceResponseDto;

    private Device device;

    private DeviceType deviceType;

    @BeforeEach
    void setup() {

        deviceRequestDto = new DeviceRequestDto(DEVICE_NAME, 1L);
        deviceTypeRequestDto = new DeviceTypeRequestDto(DEVICE_TYPE_NAME);
        deviceTypeResponseDto = new DeviceTypeResponseDto(DEVICE_TYPE_ID, DEVICE_TYPE_NAME);
        deviceResponseDto = new DeviceResponseDto(DEVICE_ID, DEVICE_NAME, deviceTypeResponseDto);

        deviceType = DeviceType.builder()
            .id(DEVICE_TYPE_ID)
            .name(DEVICE_TYPE_NAME)
            .build();

        device = Device.builder()
            .id(DEVICE_ID)
            .systemName(DEVICE_NAME)
            .type(DeviceType.builder()
                .id(DEVICE_TYPE_ID)
                .name(DEVICE_TYPE_NAME)
                .build())
            .build();
    }

    @Test
    void getDeviceById() throws Exception {
        when(service.getDeviceEntity(DEVICE_ID)).thenReturn(Optional.of(device));

        mockMvc.perform(get("/device/" + DEVICE_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(deviceResponseDto)));
    }

    @Test
    void postDevice() throws Exception {
        when(service.saveDeviceEntity(deviceRequestDto)).thenReturn(device);

        mockMvc.perform(post("/device")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deviceRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(deviceResponseDto)));
    }

    @Test
    void deleteDevice() throws Exception {
        doNothing().when(service).deleteDeviceEntity(DEVICE_ID);

        mockMvc.perform(delete("/device/" + DEVICE_ID))
            .andExpect(status().isNoContent());
    }

    @Test
    void postDeviceType() throws Exception {
        when(service.saveDeviceTypeEntity(deviceTypeRequestDto)).thenReturn(deviceType);

        mockMvc.perform(post("/device/type")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deviceTypeRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(deviceTypeResponseDto)));
    }

    @Test
    void getDeviceType() throws Exception {
        when(service.getAllDeviceType()).thenReturn(List.of(deviceType));

        mockMvc.perform(get("/device/type"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                objectMapper.writeValueAsString(List.of(deviceTypeResponseDto))));
    }

    @Test
    void getDevice() throws Exception {
        when(service.getAllDeviceEntity()).thenReturn(List.of(device));

        mockMvc.perform(get("/device"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                objectMapper.writeValueAsString(List.of(deviceResponseDto))));
    }

}
