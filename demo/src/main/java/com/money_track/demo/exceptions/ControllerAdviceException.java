package com.money_track.demo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerAdviceException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",e.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExistException(AlreadyExistsException e,HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Already Exist",e.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IsNotYoursException.class)
    public ResponseEntity<ErrorResponse> isNotYoursException(IsNotYoursException e,HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Is Not Yours",e.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NegativeNumberException.class)
    public ResponseEntity<ErrorResponse> NegativeNumbersException(NegativeNumberException e,HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Negative Numbers",e.getMessage(),request.getRequestURI(), Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
