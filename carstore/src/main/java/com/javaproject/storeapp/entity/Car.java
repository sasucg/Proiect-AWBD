package com.javaproject.storeapp.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private String name;
    private String description;
    private double price;

    private int stock;

    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CarCategory carCategory;

    @Lob
    private Byte[] image;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Review> reviewList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setcarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Car() {
    }

    public Car(String name, String description, double price, CarCategory carCategory, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.carCategory = carCategory;
        this.stock = stock;
    }

    public Car(int id, String name, String description, double price, CarCategory carCategory, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.carCategory = carCategory;
        this.stock = stock;
    }
}
