package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.ServiceDetailResponseDto;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import com.ninjaone.backendinterviewproject.service.ServiceDetailService;
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

import java.math.BigDecimal;
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
@WebMvcTest(ServiceDetailController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class ServiceDetailOperationsTest {

    public static final Long SERVICE_DETAIL_ID = 123456L;
    public static final String SERVICE_DETAIL_NAME = "TestServiceDetail";
    public static final Long DEVICE_TYPE_ID = 54321L;

    public static final String DEVICE_TYPE_NAME = "TestDeviceType";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServiceDetailService service;

    private ServiceDetailRequestDto serviceDetailRequestDto;
    private ServiceDetailResponseDto serviceDetailResponseDto;
    private ServiceDetail serviceDetail;

    @BeforeEach
    void setup() {
        serviceDetailRequestDto = new ServiceDetailRequestDto(SERVICE_DETAIL_NAME, BigDecimal.TEN, DEVICE_TYPE_ID);
        serviceDetailResponseDto = new ServiceDetailResponseDto(SERVICE_DETAIL_ID, SERVICE_DETAIL_NAME, BigDecimal.TEN);

        serviceDetail = ServiceDetail.builder()
            .id(SERVICE_DETAIL_ID)
            .name(SERVICE_DETAIL_NAME)
            .cost(BigDecimal.TEN)
            .deviceType(DeviceType.builder()
                .id(DEVICE_TYPE_ID)
                .name(DEVICE_TYPE_NAME)
                .build())
            .build();
    }

    @Test
    void postService() throws Exception {
        when(service.saveServiceDetailEntity(serviceDetailRequestDto)).thenReturn(serviceDetail);

        mockMvc.perform(post("/service")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceDetailRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(serviceDetailResponseDto)));
    }


    @Test
    void getServiceById() throws Exception {
        when(service.getServiceDetailEntityById(SERVICE_DETAIL_ID)).thenReturn(Optional.of(serviceDetail));

        mockMvc.perform(get("/service/" + SERVICE_DETAIL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(serviceDetailResponseDto)));
    }

    @Test
    void getService() throws Exception {
        when(service.getAllServiceDetailEntity()).thenReturn(List.of(serviceDetail));

        mockMvc.perform(get("/service"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                objectMapper.writeValueAsString(List.of(serviceDetailResponseDto))));
    }

    @Test
    void deleteService() throws Exception {
        doNothing().when(service).deleteServiceDetailEntity(SERVICE_DETAIL_ID);

        mockMvc.perform(delete("/service/" + SERVICE_DETAIL_ID))
            .andExpect(status().isNoContent());
    }

}
