package com.money_track.demo.exceptions;

public class NegativeNumberException extends RuntimeException{
    public NegativeNumberException(String msg){
        super(msg);
    }
}
