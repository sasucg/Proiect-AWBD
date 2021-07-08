package com.javaproject.storeapp.service;


import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.CarRepository;
import com.javaproject.storeapp.service.impl.CarServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;


    @Test
    @DisplayName("Add a new Car")
    public void createCarTest() {
        //arrange
        Car car = new Car("Mercedes", "G-Class", 60.0, CarCategory.OFFROAD, 10);
        Car savedCar = new Car("Mercedes", "G-Class", 60.0, CarCategory.OFFROAD, 10);
        when(carRepository.save(car)).thenReturn(savedCar);

        //act
        Car result = carService.createCar(car);

        //assert
        assertEquals(car.getName(), result.getName());
        assertEquals(car.getDescription(), result.getDescription());
        assertEquals(car.getPrice(), result.getPrice());
        assertEquals(car.getCarCategory(), result.getCarCategory());
        assertEquals(car.getStock(), result.getStock());
        verify(carRepository, Mockito.times((1))).save(car);
    }

    @Test
    @DisplayName("Find Car By Id - happy flow")
    public void findCarByIdTestHappyFlow() {

        Car car = new Car();
        car.setId(1);
        when(carRepository.findCarById(car.getId()))
                .thenReturn(car);

        Car result = carService.findCarById(car.getId());

        assertNotNull(car.getId());
        assertEquals(car.getId(), result.getId());

    }

    @Test
    @DisplayName("Find car By Id - car not found")
    public void findCarByIdTestNotFound() {

        Car car = new Car();
        car.setId(1);
        when(carRepository.findCarById(car.getId()))
                .thenReturn(null);

        RuntimeException exception = assertThrows(ResourceNotFoundException.class, () -> carService.findCarById(car.getId()));
        assertEquals("Car with Id " + car.getId() + " not found.", exception.getMessage());

    }

}
