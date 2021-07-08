package com.javaproject.storeapp.mapper;

import com.javaproject.storeapp.dto.CarRequest;
import com.javaproject.storeapp.entity.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public Car carRequestToCar(CarRequest carRequest) {
        return new Car(carRequest.getName(), carRequest.getDescription(), carRequest.getPrice(), carRequest.getcarCategory(), carRequest.getStock());
    }
}
