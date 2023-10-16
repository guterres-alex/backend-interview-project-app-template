package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerDeviceServicesResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.CustomerResponseDto;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.service.CustomerService;
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
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class CustomerOperationsTest {

    public static final Long DEVICE_ID = 12345L;
    public static final Long CUSTOMER_ID = 12345L;
    public static final String CUSTOMER_NAME = "TestCustomer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CustomerService service;

    private CustomerRequestDto customerRequestDto;
    private Customer customer;
    private CustomerResponseDto customerResponseDto;
    private CustomerDeviceServicesRequestDto customerDeviceServicesRequestDto;
    private CustomerDeviceServicesResponseDto customerDeviceServicesResponseDto;


    @BeforeEach
    void setup() {

        customerRequestDto = new CustomerRequestDto(CUSTOMER_NAME);
        customerResponseDto = new CustomerResponseDto(CUSTOMER_ID, CUSTOMER_NAME, null, null);
        customer = Customer.builder()
            .id(CUSTOMER_ID)
            .name(CUSTOMER_NAME)
            .build();

        customerDeviceServicesRequestDto = new CustomerDeviceServicesRequestDto(DEVICE_ID, List.of(1L, 2L));
        customerDeviceServicesResponseDto = new CustomerDeviceServicesResponseDto(CUSTOMER_ID, CUSTOMER_NAME, null);

    }

    @Test
    void postCustomer() throws Exception {
        when(service.saveCustomerEntity(customerRequestDto)).thenReturn(customer);

        mockMvc.perform(post("/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(customerResponseDto)));
    }

    @Test
    void getCustomerById() throws Exception {
        when(service.getCustomerEntity(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customer/" + CUSTOMER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(customerResponseDto)));
    }

    @Test
    void deleteCustomer() throws Exception {
        doNothing().when(service).deleteCustomerEntity(CUSTOMER_ID);

        mockMvc.perform(delete("/customer/" + CUSTOMER_ID))
            .andExpect(status().isNoContent());
    }

    @Test
    void postCustomerDeviceService() throws Exception {
        when(service.assignNewDeviceServices(CUSTOMER_ID, List.of(customerDeviceServicesRequestDto))).thenReturn(customer);

        mockMvc.perform(post("/customer/{customer-id}/assign", CUSTOMER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(List.of(customerDeviceServicesRequestDto))))
            .andExpect(status().isCreated())
            .andExpect(content().string(objectMapper.writeValueAsString(customerDeviceServicesResponseDto)));
    }

    @Test
    void getCustomerCalculation() throws Exception {
        when(service.getCalculations(CUSTOMER_ID)).thenReturn(BigDecimal.ONE);

        mockMvc.perform(get("/customer/{customer-id}/calculate", CUSTOMER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(BigDecimal.ONE)));
    }

}
