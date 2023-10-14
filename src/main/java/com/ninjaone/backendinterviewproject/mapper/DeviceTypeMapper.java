package com.ninjaone.backendinterviewproject.mapper;

import com.ninjaone.backendinterviewproject.dto.request.DeviceTypeRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.DeviceTypeResponseDto;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeviceTypeMapper {

    DeviceTypeMapper INSTANCE = Mappers.getMapper(DeviceTypeMapper.class);

    DeviceType deviceTypeRequestDtoToDeviceType(DeviceTypeRequestDto deviceTypeRequestDto);
    DeviceTypeResponseDto deviceTypeToDeviceTypeResponseDto(DeviceType deviceType);

    List<DeviceTypeResponseDto> deviceListToDeviceResponseDtoList(Iterable<DeviceType> deviceTypeList);

}
