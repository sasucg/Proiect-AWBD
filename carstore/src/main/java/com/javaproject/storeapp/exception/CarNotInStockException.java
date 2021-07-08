package com.javaproject.storeapp.exception;

public class CarNotInStockException extends RuntimeException {
    public CarNotInStockException(int id) {
        super("Not enough stock available for Car with Id " + id + ".");
    }
}
