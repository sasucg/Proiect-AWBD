package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.User;

import java.util.List;

public interface CartService {
    Cart findCartByUser(User user);

    Cart createCart(User user, double value, OrderItemRequest item);

    void resetCart(Cart cart);

    OrderItemRequest getItemBycarId(int carId, int userId);

    int updateItemQuantity(int userId, OrderItemRequest item, int quantity);

    List<OrderItemRequest> deleteItemFromCart(Cart cart, int userId, int carId);

    List<OrderItemRequest> getCartContents(int userId);

    Car validateCar(int carId, int quantity);

    Cart addcarToCart(User user, OrderItemRequest item);

    void updateCartAmount(int cartId, double value);

}
