package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.exception.DeviceException;
import com.ninjaone.backendinterviewproject.mapper.DeviceMapper;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceServicesEntity;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import com.ninjaone.backendinterviewproject.service.DeviceServicesService;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repository;
    private final DeviceTypeService deviceTypeService;
    private final DeviceServicesService deviceServicesService;
    private final MessageUtil messageUtil;

    public DeviceServiceImpl(
        DeviceRepository repository, DeviceTypeService deviceTypeService,
        DeviceServicesService deviceServicesService, MessageUtil messageUtil) {

        this.repository = repository;
        this.deviceTypeService = deviceTypeService;
        this.deviceServicesService = deviceServicesService;
        this.messageUtil = messageUtil;
    }

    public Device saveDeviceEntity(DeviceRequestDto deviceRequestDto) {
        DeviceType type = deviceTypeService.getById(deviceRequestDto.deviceTypeId());

        if (repository.findBySystemNameIgnoreCaseAndTypeId(deviceRequestDto.systemName(), deviceRequestDto.deviceTypeId())
            .isPresent()) {
            throw new DeviceException(messageUtil.getMessage("device.already.exists"));
        }

        Device device = DeviceMapper.INSTANCE.deviceRequestDtoToDevice(deviceRequestDto);
        device.setType(type);

        return repository.save(device);
    }

    public Optional<Device> getDeviceEntity(Long id) {
        return repository.findById(id);
    }

    public Iterable<Device> getAllDeviceEntity() {
        return repository.findAll();
    }

    public void deleteDeviceEntity(Long id) {
        Iterable<DeviceServicesEntity> deviceServices = deviceServicesService.findByDeviceId(id);
        if (deviceServices.iterator().hasNext()) {
            throw new DeviceException(messageUtil.getMessage(
                "device.id.used.for.customer"));
        }
        repository.deleteById(id);
    }

    public Device getDevice(Long deviceId) {
        return repository.findById(deviceId)
            .orElseThrow(() -> new DeviceException(messageUtil.getMessage("device.not.exists")));
    }

}
