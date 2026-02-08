package com.BarberHub.demo.exceptions;

public class IsNotYoursException extends RuntimeException{
    public IsNotYoursException(String message){
        super(message);
    }
}
