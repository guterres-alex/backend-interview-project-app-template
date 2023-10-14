package com.ninjaone.backendinterviewproject.mapper;

import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.ServiceDetailResponseDto;
import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ServiceDetailMapper {

    ServiceDetailMapper INSTANCE = Mappers.getMapper(ServiceDetailMapper.class);

    ServiceDetail serviceDetailRequestDtoToServiceDetail(ServiceDetailRequestDto serviceDetailRequestDto);
    ServiceDetailResponseDto serviceDetailToServiceDetailResponseDto(ServiceDetail serviceDetail);

    List<ServiceDetailResponseDto> serviceDetailListToServiceDetailResponseDtoList(Iterable<ServiceDetail> serviceDetailList);
}
