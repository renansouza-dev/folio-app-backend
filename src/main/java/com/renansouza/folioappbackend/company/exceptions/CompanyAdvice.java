package com.renansouza.folioappbackend.company.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CompanyAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CompanyNotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(CompanyNotFoundException ex, WebRequest request) {
        return buildException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    ResponseEntity<Object> handleAlreadyException(CompanyAlreadyExistsException ex, WebRequest request) {
        return buildException(ex, request, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> buildException(RuntimeException ex, WebRequest request, HttpStatus status) {
        var path = ((ServletWebRequest) request).getRequest().getRequestURI();
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("path", path);
        body.put("code", status);

        return new ResponseEntity<>(body, status);
    }

}