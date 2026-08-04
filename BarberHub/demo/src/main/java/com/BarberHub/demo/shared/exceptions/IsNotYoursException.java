package com.BarberHub.demo.shared.exceptions;

public class IsNotYoursException extends RuntimeException{
    public IsNotYoursException(String message){
        super(message);
    }
}
