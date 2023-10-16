package com.ninjaone.backendinterviewproject.service.impl;

import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.exception.DeviceTypeException;
import com.ninjaone.backendinterviewproject.mapper.DeviceTypeMapper;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
import com.ninjaone.backendinterviewproject.utils.MessageUtil;
import org.springframework.stereotype.Service;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository repository;
    private final MessageUtil messageUtil;

    public DeviceTypeServiceImpl(DeviceTypeRepository repository, MessageUtil messageUtil) {

        this.repository = repository;
        this.messageUtil = messageUtil;
    }

    public DeviceType save(DeviceTypeRequestDto deviceTypeRequestDto) {
        if (repository.findByNameIgnoreCase(deviceTypeRequestDto.name()).isPresent()) {
            throw new DeviceTypeException(messageUtil.getMessage("device.type.already.exists"));
        }

        return repository.save(DeviceTypeMapper.INSTANCE.deviceTypeRequestDtoToDeviceType(deviceTypeRequestDto));
    }

    public Iterable<DeviceType> findAll() {
        return repository.findAll();
    }

    public DeviceType getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new DeviceTypeException(messageUtil.getMessage("device.type.not.exists")));
    }

}
