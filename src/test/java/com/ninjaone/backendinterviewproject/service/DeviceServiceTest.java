package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.exception.DeviceException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.impl.DeviceServiceImpl;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
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
public class DeviceServiceTest {

    public static final Long DEVICE_ID = 12345L;
    public static final String DEVICE_NAME = "TestDevice";
    public static final Long DEVICE_TYPE_ID = 54321L;

    @InjectMocks
    private DeviceServiceImpl service;

    @Mock
    private DeviceRepository repository;
    @Mock
    private DeviceTypeService deviceTypeService;
    @Mock
    private DeviceServicesService deviceServicesService;
    @Mock
    private MessageUtil messageUtil;

    private DeviceRequestDto deviceRequestDto;
    private Device device;
    private DeviceType deviceType;

    @BeforeEach
    void setup() {

        deviceRequestDto = new DeviceRequestDto(DEVICE_NAME, DEVICE_TYPE_ID);

        deviceType = DeviceType.builder()
            .id(DEVICE_TYPE_ID)
            .build();

        device = Device.builder()
            .id(DEVICE_ID)
            .systemName(DEVICE_NAME)
            .type(deviceType)
            .build();
    }

    @Test
    void saveDeviceEntity() {

        when(deviceTypeService.getById(DEVICE_TYPE_ID)).thenReturn(deviceType);
        when(repository.findBySystemNameIgnoreCaseAndTypeId(DEVICE_NAME, deviceRequestDto.deviceTypeId()))
            .thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(device);

        Device savedDeviceEntity = service.saveDeviceEntity(deviceRequestDto);

        assertNotNull(savedDeviceEntity);
        assertEquals(device.getId(), savedDeviceEntity.getId());
        assertEquals(device.getSystemName(), savedDeviceEntity.getSystemName());
        assertEquals(device.getType().getId(), savedDeviceEntity.getType().getId());
    }

    @Test
    void saveDeviceEntityException() {

        when(deviceTypeService.getById(DEVICE_TYPE_ID)).thenReturn(deviceType);

        when(repository.findBySystemNameIgnoreCaseAndTypeId(DEVICE_NAME, deviceRequestDto.deviceTypeId()))
            .thenReturn(Optional.of(device));

        String error = "ErrorTest";
        when(messageUtil.getMessage("device.already.exists")).thenReturn(error);

        DeviceException exception = assertThrows(DeviceException.class, () ->
            service.saveDeviceEntity(deviceRequestDto));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void getDeviceEntity() {
        when(repository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        Optional<Device> deviceEntity = service.getDeviceEntity(DEVICE_ID);

        assertTrue(deviceEntity.isPresent());
        assertEquals(device.getId(), deviceEntity.get().getId());
        assertEquals(device.getSystemName(), deviceEntity.get().getSystemName());
    }

    @Test
    void getAllDeviceEntity() {

        List<Device> deviceList = List.of(device, Device.builder().id(3L).build());

        when(repository.findAll()).thenReturn(deviceList);

        Iterable<Device> allDeviceEntity = service.getAllDeviceEntity();
        Iterator<Device> iterator = allDeviceEntity.iterator();

        assertTrue(iterator.hasNext());
        Device next = iterator.next();
        assertEquals(device.getId(), next.getId());
        assertEquals(device.getSystemName(), next.getSystemName());

        assertTrue(iterator.hasNext());
        assertEquals(3L, iterator.next().getId());
    }

    @Test
    void deleteCustomerEntity() {
        when(deviceServicesService.findByDeviceId(DEVICE_ID)).thenReturn(List.of());
        doNothing().when(repository).deleteById(DEVICE_ID);

        service.deleteDeviceEntity(DEVICE_ID);

        verify(repository, times(1)).deleteById(DEVICE_ID);
    }

    @Test
    void deleteCustomerEntityException() {
        when(deviceServicesService.findByDeviceId(DEVICE_ID))
            .thenReturn(List.of(DeviceServicesEntity.builder().build()));

        String error = "ErrorTest";
        when(messageUtil.getMessage("device.id.used.for.customer")).thenReturn(error);

        DeviceException exception = assertThrows(DeviceException.class, () ->
            service.deleteDeviceEntity(DEVICE_ID));

        assertTrue(error.contains(exception.getMessage()));

    }

}
