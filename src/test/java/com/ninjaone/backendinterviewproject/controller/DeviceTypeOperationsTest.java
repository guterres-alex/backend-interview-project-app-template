package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class})
@WebMvcTest(DeviceTypeController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class DeviceTypeOperationsTest {

    public static final Long DEVICE_TYPE_ID = 54321L;
    public static final String DEVICE_TYPE_NAME = "TestDeviceType";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceTypeService service;

    private DeviceTypeRequestDto deviceTypeRequestDto;

    private DeviceTypeResponseDto deviceTypeResponseDto;

    private DeviceType deviceType;

    @BeforeEach
    void setup() {

        deviceTypeRequestDto = new DeviceTypeRequestDto(DEVICE_TYPE_NAME);
        deviceTypeResponseDto = new DeviceTypeResponseDto(DEVICE_TYPE_ID, DEVICE_TYPE_NAME);

        deviceType = DeviceType.builder()
            .id(DEVICE_TYPE_ID)
            .name(DEVICE_TYPE_NAME)
            .build();
    }


    @Test
    void postDeviceType() throws Exception {
        when(service.save(deviceTypeRequestDto)).thenReturn(deviceType);

        mockMvc.perform(post("/device/type")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deviceTypeRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(deviceTypeResponseDto)));
    }

    @Test
    void getDeviceType() throws Exception {
        when(service.findAll()).thenReturn(List.of(deviceType));

        mockMvc.perform(get("/device/type"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                objectMapper.writeValueAsString(List.of(deviceTypeResponseDto))));
    }

}
