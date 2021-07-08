package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {

    Car createCar(Car car);

    Car findCarById(int id);

    Page<Car> getCarsBy(String category, String name, boolean descending, Pageable pageable);

    Car updateStock(int carId, int stock);

    void updateRating(Car car, double value);
}
