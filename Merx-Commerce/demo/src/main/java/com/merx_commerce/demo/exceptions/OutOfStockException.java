package com.merx_commerce.demo.exceptions;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String msg){
        super(msg);
    }
}
