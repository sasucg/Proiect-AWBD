package com.javaproject.storeapp.repository;


import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findCarById(int id);

    default List<Car> getCarsBy(String category, String name, boolean descending) {//, Pageable pageable) {
        return this.findAll().stream()
                .filter(car -> {
                    if (category != null) {//we filter by category only if the category was sent in the request
                        if (name != null && !name.equals("")) { //we filter by name only if the name was sent in the request
                            return CarCategory.valueOf(category.toUpperCase()).equals(car.getCarCategory()) && car.getName().toLowerCase().contains(name.toLowerCase());
                        } else {
                            return CarCategory.valueOf(category.toUpperCase()).equals(car.getCarCategory());
                        }
                    } else if (name != null && !name.equals("")) { //we filter by name only if the name was sent in the request
                        return car.getName().toLowerCase().contains(name.toLowerCase());
                    } else {//no filters are sent in the request, all cars should be returned
                        return true;
                    }
                })
                .sorted(descending ? Comparator.comparingDouble(Car::getPrice).reversed() : Comparator.comparingDouble(Car::getPrice))
                .collect(Collectors.toList());

    }
}