package com.merx_commerce.demo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerAdviceException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException exception, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),"Not found", exception.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExistException(AlreadyExistsException exception, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Already Exists", exception.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
