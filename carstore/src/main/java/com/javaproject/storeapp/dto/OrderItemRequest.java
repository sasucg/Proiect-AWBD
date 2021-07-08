package com.javaproject.storeapp.dto;

import com.javaproject.storeapp.entity.Car;
import lombok.Data;

@Data
public class OrderItemRequest {
    public Car getcar() {
        return car;
    }

    public void setcar(Car car) {
        this.car = car;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private Car car;
    private int quantity;
    private double price;

    public OrderItemRequest(Car car, int quantity, double price) {
        this.car = car;
        this.quantity = quantity;
        this.price = price;
    }

}
