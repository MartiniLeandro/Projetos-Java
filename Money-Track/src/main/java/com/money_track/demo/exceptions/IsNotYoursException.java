package com.money_track.demo.exceptions;

public class IsNotYoursException extends RuntimeException{
    public IsNotYoursException(String msg){
        super(msg);
    }
}
