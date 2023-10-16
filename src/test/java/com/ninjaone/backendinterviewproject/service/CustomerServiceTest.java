package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesChangeRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerDeviceServicesRequestDto;
import com.ninjaone.backendinterviewproject.dto.request.CustomerRequestDto;
import com.ninjaone.backendinterviewproject.exception.CustomerException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import com.ninjaone.backendinterviewproject.service.impl.CustomerServiceImpl;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    public static final Long ID = 12345L;
    public static final String NAME = "TestCustomer";

    @InjectMocks
    private CustomerServiceImpl service;

    @Mock
    private CustomerRepository repository;
    @Mock
    private DeviceService deviceService;
    @Mock
    private ServiceDetailService serviceDetailService;
    @Mock
    private DeviceServicesService deviceServicesService;
    @Mock
    private MessageUtil messageUtil;

    final ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
    final ArgumentCaptor<DeviceServicesEntity> deviceServicesCaptor = ArgumentCaptor.forClass(DeviceServicesEntity.class);

    private CustomerRequestDto customerRequestDto;
    private Customer customer;
    private Customer customerResp;

    @BeforeEach
    void setup() {
        customerRequestDto = new CustomerRequestDto(NAME);

        customer = Customer.builder()
            .name(NAME)
            .build();

        customerResp = Customer.builder()
            .id(ID)
            .name(NAME)
            .deviceServiceEntities(new ArrayList<>())
            .build();
    }

    @Test
    void saveCustomerEntity() {

        when(repository.save(customer)).thenReturn(customerResp);

        Customer savedCustomerEntity = service.saveCustomerEntity(customerRequestDto);

        assertNotNull(savedCustomerEntity);
        assertEquals(customerResp.getId(), savedCustomerEntity.getId());
        assertEquals(customerResp.getName(), savedCustomerEntity.getName());
    }

    @Test
    void getCustomerEntity() {
        when(repository.findById(ID)).thenReturn(Optional.of(customerResp));

        Optional<Customer> customerEntity = service.getCustomerEntity(ID);

        assertTrue(customerEntity.isPresent());
        assertEquals(customerResp.getId(), customerEntity.get().getId());
        assertEquals(customerResp.getName(), customerEntity.get().getName());
    }

    @Test
    void deleteCustomerEntity() {
        doNothing().when(repository).deleteById(ID);
        service.deleteCustomerEntity(ID);
        Mockito.verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void assignNewDeviceServices() {

        when(repository.findById(ID)).thenReturn(Optional.of(customerResp));

        DeviceType deviceType = DeviceType.builder()
            .id(1L)
            .name("Type1")
            .build();

        when(deviceService.getDevice(1L)).thenReturn(
            Device.builder()
                .id(1L)
                .systemName("Device")
                .type(deviceType)
                .build());

        when(serviceDetailService.getServicesByDeviceType(deviceType, List.of(1L, 2L, 3L))).thenReturn(
            new ArrayList<>(Arrays.asList(
                ServiceDetail.builder().name("ServiceA").cost(BigDecimal.TEN).deviceType(deviceType).build(),
                ServiceDetail.builder().name("ServiceB").cost(BigDecimal.ONE).deviceType(deviceType).build(),
                ServiceDetail.builder().name("ServiceC").cost(BigDecimal.ZERO).deviceType(deviceType).build())));

        when(serviceDetailService.getAutomaticServices()).thenReturn(
            List.of(ServiceDetail.builder().name("ServiceAutomatic").cost(BigDecimal.ONE).automatic(true).build()));

        CustomerDeviceServicesRequestDto requestDto =
            new CustomerDeviceServicesRequestDto(1L, List.of(1L, 2L, 3L));

        when(repository.save(any())).thenReturn(Customer.builder().build());

        service.assignNewDeviceServices(ID, List.of(requestDto));

        verify(repository).save(customerCaptor.capture());
        verify(deviceServicesService).save(deviceServicesCaptor.capture());

        DeviceServicesEntity deviceServicesEntity = deviceServicesCaptor.getValue();
        Customer customer = customerCaptor.getValue();

        assertNotNull(customer);
        assertEquals(customerResp.getName(), customer.getName());
        assertEquals(new BigDecimal(12), deviceServicesEntity.getTotalCost());
    }

    @Test
    void assignServicesExistentDeviceServices() {
        DeviceType deviceType = DeviceType.builder()
            .id(1L)
            .name("Type1")
            .build();

        Device device = Device.builder()
            .id(1L)
            .systemName("Device")
            .type(deviceType)
            .build();

        customerResp.setDeviceServiceEntities(
            List.of(DeviceServicesEntity.builder()
                .id(1L)
                .device(device)
                .build()));

        when(repository.findById(ID)).thenReturn(Optional.of(customerResp));

        when(serviceDetailService.getServicesByDeviceType(deviceType, List.of(1L, 2L, 3L))).thenReturn(
            Arrays.asList(
                ServiceDetail.builder().name("ServiceA").cost(BigDecimal.TEN).deviceType(deviceType).build(),
                ServiceDetail.builder().name("ServiceB").cost(BigDecimal.ONE).deviceType(deviceType).build(),
                ServiceDetail.builder().name("ServiceC").cost(BigDecimal.ZERO).deviceType(deviceType).build()));

        when(serviceDetailService.getAutomaticServices()).thenReturn(new ArrayList<>());

        CustomerDeviceServicesChangeRequestDto requestDto =
            new CustomerDeviceServicesChangeRequestDto(1L, List.of(1L, 2L, 3L));


        when(repository.save(any())).thenReturn(Customer.builder().build());

        service.assignServicesExistentDeviceServices(ID, List.of(requestDto));

        verify(repository).save(customerCaptor.capture());

        Customer customer = customerCaptor.getValue();

        assertNotNull(customer);
        assertEquals(customerResp.getName(), customer.getName());
        assertEquals(new BigDecimal(11), customerResp.getDeviceServiceEntities().get(0).getTotalCost());
    }

    @Test
    void assignServicesExistentDeviceServicesException() {
        DeviceType deviceType = DeviceType.builder()
            .id(1L)
            .name("Type1")
            .build();

        Device device = Device.builder()
            .id(1L)
            .systemName("Device")
            .type(deviceType)
            .build();

        customerResp.setDeviceServiceEntities(
            List.of(DeviceServicesEntity.builder()
                .id(2L)
                .device(device)
                .build()));

        when(repository.findById(ID)).thenReturn(Optional.of(customerResp));

        CustomerDeviceServicesChangeRequestDto requestDto =
            new CustomerDeviceServicesChangeRequestDto(1L, List.of(1L, 2L, 3L));

        String error = "ErrorTest";
        when(messageUtil.getMessage("service.device.not.exists", new Object[]{1L})).thenReturn(error);

        CustomerException exception = assertThrows(CustomerException.class, () ->
            service.assignServicesExistentDeviceServices(ID, List.of(requestDto))
        );

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test()
    void getCalculations() {

        Customer customer = Customer.builder()
            .deviceServiceEntities(List.of(
                DeviceServicesEntity.builder()
                    .totalCost(BigDecimal.TEN)
                    .build(),
                DeviceServicesEntity.builder()
                    .totalCost(BigDecimal.TEN)
                    .build(),
                DeviceServicesEntity.builder()
                    .totalCost(BigDecimal.ONE)
                    .build(),
                DeviceServicesEntity.builder()
                    .totalCost(BigDecimal.ONE)
                    .build()
            ))
            .build();

        when(repository.findById(ID)).thenReturn(Optional.of(customer));

        BigDecimal calculations = service.getCalculations(ID);

        assertEquals(new BigDecimal("22"), calculations);
    }

    @Test()
    void getDynamicCalculations() {

        Customer customer = Customer.builder()
            .deviceServiceEntities(List.of(
                DeviceServicesEntity.builder()
                    .services(List.of(
                        ServiceDetail.builder()
                            .cost(BigDecimal.TEN)
                            .build(),
                        ServiceDetail.builder()
                            .cost(BigDecimal.TEN)
                            .build(),
                        ServiceDetail.builder()
                            .cost(BigDecimal.TEN)
                            .build()))
                    .build(),
                DeviceServicesEntity.builder()
                    .services(List.of(
                        ServiceDetail.builder()
                            .cost(BigDecimal.TEN)
                            .build(),
                        ServiceDetail.builder()
                            .cost(BigDecimal.ONE)
                            .build()))
                    .build(),
                DeviceServicesEntity.builder()
                    .services(List.of(
                        ServiceDetail.builder()
                            .cost(BigDecimal.TEN)
                            .build()))
                    .build(),
                DeviceServicesEntity.builder()
                    .services(List.of(
                        ServiceDetail.builder()
                            .cost(BigDecimal.ONE)
                            .build()))
                    .build()
            ))
            .build();

        when(repository.findById(ID)).thenReturn(Optional.of(customer));

        BigDecimal calculations = service.getDynamicCalculations(ID);

        assertEquals(new BigDecimal("52"), calculations);
    }

    @Test()
    void getCalculationsException() {

        when(repository.findById(any())).thenReturn(Optional.empty());

        String error = "ErrorTest";
        when(messageUtil.getMessage("customer.not.exists")).thenReturn(error);

        CustomerException exception = assertThrows(CustomerException.class, () ->
            service.getCalculations(RandomUtils.nextLong())
        );

        assertTrue(error.contains(exception.getMessage()));
    }

}
