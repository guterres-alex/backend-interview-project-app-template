package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.exception.DeviceTypeException;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.impl.DeviceTypeServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceTypeServiceTest {

    public static final String DEVICE_TYPE_NAME = "TestDevice";
    public static final Long DEVICE_TYPE_ID = 54321L;

    @InjectMocks
    private DeviceTypeServiceImpl service;

    @Mock
    private DeviceTypeRepository repository;
    @Mock
    private MessageUtil messageUtil;

    private DeviceTypeRequestDto deviceTypeRequestDto;
    private DeviceType deviceType;

    @BeforeEach
    void setup() {

        deviceTypeRequestDto = new DeviceTypeRequestDto(DEVICE_TYPE_NAME);

        deviceType = DeviceType.builder()
            .id(DEVICE_TYPE_ID)
            .build();
    }

    @Test
    void saveDeviceTypeEntity() {

        when(repository.findByNameIgnoreCase(DEVICE_TYPE_NAME))
            .thenReturn(Optional.empty());

        when(repository.save(any())).thenReturn(deviceType);

        DeviceType savedDeviceType = service.save(deviceTypeRequestDto);

        assertNotNull(savedDeviceType);
        assertEquals(deviceType.getId(), savedDeviceType.getId());
        assertEquals(deviceType.getName(), savedDeviceType.getName());
    }

    @Test
    void saveDeviceTypeEntityException() {

        when(repository.findByNameIgnoreCase(DEVICE_TYPE_NAME))
            .thenReturn(Optional.of(deviceType));

        String error = "ErrorTest";
        when(messageUtil.getMessage("device.type.already.exists")).thenReturn(error);

        DeviceTypeException exception = assertThrows(DeviceTypeException.class, () ->
            service.save(deviceTypeRequestDto));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void getAllDeviceTypeEntity() {

        List<DeviceType> deviceTypeList = List.of(deviceType, DeviceType.builder().id(3L).build());

        when(repository.findAll()).thenReturn(deviceTypeList);

        Iterable<DeviceType> allDeviceEntity = service.findAll();
        Iterator<DeviceType> iterator = allDeviceEntity.iterator();

        assertTrue(iterator.hasNext());
        DeviceType next = iterator.next();
        assertEquals(deviceType.getId(), next.getId());
        assertEquals(deviceType.getName(), next.getName());

        assertTrue(iterator.hasNext());
        assertEquals(3L, iterator.next().getId());
    }

    @Test
    void getDeviceTypeEntityById() {
        when(repository.findById(DEVICE_TYPE_ID)).thenReturn(Optional.of(deviceType));

        DeviceType savedDeviceType = service.getById(DEVICE_TYPE_ID);

        assertEquals(deviceType.getId(), savedDeviceType.getId());
        assertEquals(deviceType.getName(), savedDeviceType.getName());
    }

    @Test
    void getDeviceTypeEntityByIdException() {
        when(repository.findById(DEVICE_TYPE_ID)).thenReturn(Optional.empty());

        String error = "ErrorTest";
        when(messageUtil.getMessage("device.type.not.exists")).thenReturn(error);

        DeviceTypeException exception = assertThrows(DeviceTypeException.class, () ->
            service.getById(DEVICE_TYPE_ID));

        assertTrue(error.contains(exception.getMessage()));
    }

}
