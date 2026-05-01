package com.BarberHub.demo.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
