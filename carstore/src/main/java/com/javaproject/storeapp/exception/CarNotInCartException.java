package com.javaproject.storeapp.exception;

public class CarNotInCartException extends RuntimeException {
    public CarNotInCartException(int id) {
        super("Car with Id " + id + " not in cart.");
    }
}
