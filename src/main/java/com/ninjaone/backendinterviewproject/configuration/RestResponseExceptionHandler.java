package com.ninjaone.backendinterviewproject.configuration;

import com.ninjaone.backendinterviewproject.dto.ErrorResponseDto;
import com.ninjaone.backendinterviewproject.exception.CustomerException;
import com.ninjaone.backendinterviewproject.exception.CustomerNotFoundException;
import com.ninjaone.backendinterviewproject.exception.DeviceException;
import com.ninjaone.backendinterviewproject.exception.DeviceNotFoundException;
import com.ninjaone.backendinterviewproject.exception.DeviceTypeException;
import com.ninjaone.backendinterviewproject.exception.DeviceTypeNotFoundException;
import com.ninjaone.backendinterviewproject.exception.ServiceDetailException;
import com.ninjaone.backendinterviewproject.exception.ServiceDetailNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> handleRequestException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleRequestDeviceException(DeviceException exception) {
        return new ResponseEntity<>(new ErrorResponseDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleRequestDeviceNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeviceTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleRequestDeviceTypeException(DeviceTypeException exception) {
        return new ResponseEntity<>(new ErrorResponseDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleRequestDeviceTypeNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleRequestCustomerException(CustomerException exception) {
        return new ResponseEntity<>(new ErrorResponseDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleRequestCustomerNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceDetailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleRequestServiceDetailException(ServiceDetailException exception) {
        return new ResponseEntity<>(new ErrorResponseDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceDetailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleRequestServiceDetailNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", errors));
    }

}
