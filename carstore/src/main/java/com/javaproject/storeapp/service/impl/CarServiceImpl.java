package com.javaproject.storeapp.service.impl;

import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.CarRepository;
import com.javaproject.storeapp.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    @Override
    public Car createCar(Car p) {
        return carRepository.save(p);
    }

    @Override
    public Car findCarById(int id) {
        Optional<Car> carOptional = Optional.ofNullable(carRepository.findCarById(id));
        if (carOptional.isPresent()) {
            return carOptional.get();
        } else {
            throw new ResourceNotFoundException("Car with Id " + id + " not found.");
        }
    }

    @Override
    public Page<Car> getCarsBy(String category, String name, boolean descending, Pageable pageable) {
        if (category != null && !category.equals("null")) {
            String upperCaseCategory = category.toUpperCase();
            if (!CarCategory.contains(upperCaseCategory))
                throw new ResourceNotFoundException("Category " + category + " not found.");
        }
        if (category != null && category.equals("null"))
            category = null;
        List<Car> cars = carRepository.getCarsBy(category, name, descending);
        return findPaginated(cars, pageable);
    }

    private Page<Car> findPaginated(List<Car> cars, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Car> result;

        if (cars.size() < startItem) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, cars.size());
            result = cars.subList(startItem, toIndex);
        }

        return new PageImpl<>(result, PageRequest.of(currentPage, pageSize), cars.size());//descending ? Sort.by("price").descending() : Sort.by("price").ascending()), cars.size());
    }

    @Override
    public Car updateStock(int carId, int stock) {
        Car car = findCarById(carId);
        car.setStock(stock);
        return carRepository.save(car);
    }

    @Override
    public void updateRating(Car car, double value) {
        car.setRating(value);
        carRepository.save(car);
    }

}

