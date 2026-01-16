package com.alvin.app.Exceptions;

public class InsufficientStockException extends Exception{
    public InsufficientStockException(String error){
        super(error);
    }
}
