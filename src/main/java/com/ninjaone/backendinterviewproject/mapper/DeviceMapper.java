package com.ninjaone.backendinterviewproject.mapper;

import com.ninjaone.backendinterviewproject.dto.request.DeviceRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceResponseDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    Device deviceRequestDtoToDevice(DeviceRequestDto deviceRequestDto);
    DeviceResponseDto deviceToDeviceResponseDto(Device device);

    List<DeviceResponseDto> deviceListToDeviceResponseDtoList(Iterable<Device> deviceList);

}
