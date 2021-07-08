package com.javaproject.storeapp.service.impl;

import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.Review;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.ReviewRepository;
import com.javaproject.storeapp.service.CarService;
import com.javaproject.storeapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CarService carService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, CarService carService) {
        this.reviewRepository = reviewRepository;
        this.carService = carService;
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForcar(int carId) {
        Car car = carService.findCarById(carId);
        return reviewRepository.findReviewsBycar(carId);
    }

    @Override
    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    @Override
    public Review findReviewById(int id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        } else {
            throw new ResourceNotFoundException("Review with Id " + id + " not found.");
        }
    }
}
