package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.dto.request.ServiceDetailRequestDto;
import com.ninjaone.backendinterviewproject.dto.response.ServiceDetailResponseDto;
import com.ninjaone.backendinterviewproject.exception.ServiceDetailNotFoundException;
import com.ninjaone.backendinterviewproject.mapper.ServiceDetailMapper;
import com.ninjaone.backendinterviewproject.service.ServiceDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/service")
public class ServiceDetailController implements ServiceDetailOperations {

    private final ServiceDetailService service;

    public ServiceDetailController(ServiceDetailService service) {
        this.service = service;
    }

    public ServiceDetailResponseDto postServiceDetailEntity(ServiceDetailRequestDto serviceDto) {
        return ofNullable(service.saveServiceDetailEntity(serviceDto))
            .map(ServiceDetailMapper.INSTANCE::serviceDetailToServiceDetailResponseDto)
            .orElseThrow();
    }

    public ServiceDetailResponseDto getServiceDetailEntity(Long id) {
        return service.getServiceDetailEntityById(id)
            .map(ServiceDetailMapper.INSTANCE::serviceDetailToServiceDetailResponseDto)
            .orElseThrow(ServiceDetailNotFoundException::new);
    }

    public List<ServiceDetailResponseDto> getAllServiceDetailEntity() {
        return ofNullable(service.getAllServiceDetailEntity())
            .map(ServiceDetailMapper.INSTANCE::serviceDetailListToServiceDetailResponseDtoList)
            .orElseThrow(ServiceDetailNotFoundException::new);
    }

    public void deleteServiceDetailEntity(Long id) {
        service.deleteServiceDetailEntity(id);
    }

}
