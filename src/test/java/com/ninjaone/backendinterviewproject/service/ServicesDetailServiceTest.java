package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServiceDetailRepository;
import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.exception.ServiceDetailException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import com.ninjaone.backendinterviewproject.service.impl.ServiceDetailServiceImpl;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicesDetailServiceTest {

    public static final Long ID = 12345L;
    public static final String NAME = "ServiceDetailTest";
    public static final Long DEVICE_TYPE_ID = 54321L;

    @InjectMocks
    private ServiceDetailServiceImpl service;

    @Mock
    private ServiceDetailRepository repository;
    @Mock
    private DeviceTypeService deviceTypeService;
    @Mock
    private DeviceServicesService deviceServicesService;
    @Mock
    private MessageUtil messageUtil;

    private ServiceDetailRequestDto serviceDetailRequestDto;
    private DeviceType deviceType;

    private ServiceDetail serviceDetail;

    @BeforeEach
    void setup() {
        serviceDetailRequestDto = new ServiceDetailRequestDto(NAME, BigDecimal.TEN, DEVICE_TYPE_ID);
        deviceType = DeviceType.builder()
            .id(DEVICE_TYPE_ID)
            .build();

        serviceDetail = ServiceDetail.builder()
            .id(ID)
            .name(NAME)
            .deviceType(deviceType)
            .build();
    }

    @Test
    void save() {

        when(repository.findByNameIgnoreCaseAndDeviceTypeId(NAME, DEVICE_TYPE_ID)).thenReturn(Optional.empty());
        when(deviceTypeService.getById(DEVICE_TYPE_ID)).thenReturn(deviceType);

        when(repository.save(any())).thenReturn(serviceDetail);

        ServiceDetail savedServiceDetailEntity = service.saveServiceDetailEntity(serviceDetailRequestDto);

        assertNotNull(savedServiceDetailEntity);
        assertEquals(serviceDetail.getId(), savedServiceDetailEntity.getId());
        assertEquals(serviceDetail.getName(), savedServiceDetailEntity.getName());
        assertEquals(serviceDetail.getDeviceType().getId(), savedServiceDetailEntity.getDeviceType().getId());
    }

    @Test
    void saveException() {

        when(repository.findByNameIgnoreCaseAndDeviceTypeId(NAME, DEVICE_TYPE_ID)).thenReturn(Optional.of(serviceDetail));

        String error = "ErrorTest";
        when(messageUtil.getMessage("service.already.exists")).thenReturn(error);

        ServiceDetailException exception = assertThrows(ServiceDetailException.class, () ->
            service.saveServiceDetailEntity(serviceDetailRequestDto));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void findServiceDetailEntityById() {

        when(repository.findById(ID)).thenReturn(Optional.of(serviceDetail));

        Optional<ServiceDetail> serviceDetailEntityById = service.findServiceDetailEntityById(ID);

        assertTrue(serviceDetailEntityById.isPresent());
        assertEquals(serviceDetail.getId(), serviceDetailEntityById.get().getId());
        assertEquals(serviceDetail.getName(), serviceDetailEntityById.get().getName());
        assertEquals(serviceDetail.getDeviceType().getId(), serviceDetailEntityById.get().getDeviceType().getId());

    }

    @Test
    void findAllServiceDetailEntity() {

        when(repository.findAll()).thenReturn(
            List.of(serviceDetail, ServiceDetail.builder().id(33L).build()));

        Iterator<ServiceDetail> iterator = service.findAllServiceDetailEntity().iterator();

        assertTrue(iterator.hasNext());
        ServiceDetail next = iterator.next();
        assertEquals(serviceDetail.getId(), next.getId());
        assertEquals(serviceDetail.getName(), next.getName());
        assertEquals(serviceDetail.getDeviceType().getId(), next.getDeviceType().getId());

        assertTrue(iterator.hasNext());
        assertEquals(33L, iterator.next().getId());
    }

    @Test
    void deleteServiceDetailEntity() {
        when(deviceServicesService.findByServicesId(ID)).thenReturn(List.of());
        doNothing().when(repository).deleteById(ID);

        service.deleteServiceDetailEntity(ID);

        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void deleteServiceDetailEntityException() {
        when(deviceServicesService.findByServicesId(ID)).thenReturn(List.of(
            DeviceServicesEntity.builder()
                .device(Device.builder()
                    .id(88L)
                    .build())
                .build()));

        String error = "ErrorTest";
        when(messageUtil.getMessage("service.id.used.for.customer", new Object[]{88L})).thenReturn(error);

        ServiceDetailException exception = assertThrows(ServiceDetailException.class, () ->
            service.deleteServiceDetailEntity(ID));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void getServicesByDeviceType() {

        when(repository.findById(ID)).thenReturn(Optional.of(serviceDetail));

        ServiceDetail serviceDetail1 = ServiceDetail.builder().id(33L).deviceType(deviceType).build();
        when(repository.findById(serviceDetail1.getId())).thenReturn(Optional.of(serviceDetail1));

        List<ServiceDetail> resultList = service.getServicesByDeviceType(deviceType, List.of(ID, serviceDetail1.getId()));

        assertFalse(resultList.isEmpty());
        assertEquals(serviceDetail.getId(), resultList.get(0).getId());
        assertEquals(serviceDetail1.getId(), resultList.get(1).getId());
    }

    @Test
    void getServicesByDeviceTypeExceptionServiceNotExists() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        String error = "ErrorTest";
        when(messageUtil.getMessage("service.not.exists")).thenReturn(error);

        ServiceDetailException exception = assertThrows(ServiceDetailException.class, () ->
            service.getServicesByDeviceType(deviceType, List.of(ID)));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void getServicesByDeviceTypeExceptionDifferentType() {

        when(repository.findById(ID)).thenReturn(Optional.of(serviceDetail));

        String error = "ErrorTest";
        when(messageUtil.getMessage("service.type.is.different.from", new Object[]{"testDifferentType"}))
            .thenReturn(error);

        ServiceDetailException exception = assertThrows(ServiceDetailException.class, () ->
            service.getServicesByDeviceType(DeviceType.builder().name("testDifferentType").build(), List.of(ID)));

        assertTrue(error.contains(exception.getMessage()));
    }

    @Test
    void getAutomaticServices() {

        when(repository.findByAutomatic(true)).thenReturn(
            List.of(serviceDetail, ServiceDetail.builder().id(55L).build()));

        Iterator<ServiceDetail> iterator = service.getAutomaticServices().iterator();

        assertTrue(iterator.hasNext());
        ServiceDetail next = iterator.next();
        assertEquals(serviceDetail.getId(), next.getId());
        assertEquals(serviceDetail.getName(), next.getName());
        assertEquals(serviceDetail.getDeviceType().getId(), next.getDeviceType().getId());

        assertTrue(iterator.hasNext());
        assertEquals(55L, iterator.next().getId());
    }

}
