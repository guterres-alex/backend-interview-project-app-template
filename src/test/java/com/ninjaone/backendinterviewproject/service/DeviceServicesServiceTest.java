package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceServicesRepository;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.service.impl.DeviceServicesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServicesServiceTest {

    public static final Long ID = 12345L;

    @InjectMocks
    private DeviceServicesServiceImpl service;

    @Mock
    private DeviceServicesRepository repository;

    private DeviceServicesEntity deviceServicesEntity;

    @BeforeEach
    void setup() {
        deviceServicesEntity = DeviceServicesEntity
            .builder()
            .id(ID)
            .build();
    }

    @Test
    void save() {

        when(repository.save(any())).thenReturn(deviceServicesEntity);

        DeviceServicesEntity savedDeviceServicesEntity = service.save(deviceServicesEntity);

        assertNotNull(savedDeviceServicesEntity);
        assertEquals(deviceServicesEntity.getId(), savedDeviceServicesEntity.getId());
    }

    @Test
    void findByDeviceId() {

        when(repository.findByDeviceId(43L)).thenReturn(
            List.of(deviceServicesEntity, DeviceServicesEntity.builder().id(11L).build()));

        Iterator<DeviceServicesEntity> iterator = service.findByDeviceId(43L).iterator();

        assertTrue(iterator.hasNext());
        DeviceServicesEntity next = iterator.next();
        assertEquals(deviceServicesEntity.getId(), next.getId());

        assertTrue(iterator.hasNext());
        assertEquals(11L, iterator.next().getId());
    }

    @Test
    void findByServicesId() {

        when(repository.findByServicesId(8L)).thenReturn(
            List.of(deviceServicesEntity, DeviceServicesEntity.builder().id(15L).build()));

        Iterator<DeviceServicesEntity> iterator = service.findByServicesId(8L).iterator();

        assertTrue(iterator.hasNext());
        DeviceServicesEntity next = iterator.next();
        assertEquals(deviceServicesEntity.getId(), next.getId());

        assertTrue(iterator.hasNext());
        assertEquals(15L, iterator.next().getId());
    }

}
