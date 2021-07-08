package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    default List<Review> findReviewsBycar(int carId) {
        return this.findAll()
                .stream()
                .filter(rev -> rev.getcar().getId() == carId)
                .collect(Collectors.toList());
    }
}
