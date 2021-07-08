package com.javaproject.storeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarCategoryNotFound extends RuntimeException {
    public CarCategoryNotFound(String name) {
        super("Car category " + name + " does not exist!");
    }
}
