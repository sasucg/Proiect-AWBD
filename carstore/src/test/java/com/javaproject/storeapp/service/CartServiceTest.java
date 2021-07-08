package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import com.javaproject.storeapp.exception.NegativeQuantityException;
import com.javaproject.storeapp.exception.CarNotInStockException;
import com.javaproject.storeapp.repository.CartRepository;
import com.javaproject.storeapp.service.impl.CartServiceImpl;
import com.javaproject.storeapp.service.impl.CarServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CarServiceImpl carService;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Get Cart Contents - happy flow")
    public void getCartContentsTestHappyFlow() {
        Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
        Car car = new Car();
        car.setId(1);
        cartItems.put(1, Collections.singletonList(new OrderItemRequest(car, 1, 50)));
        int customerId = 1;
        cartService.setCartItems(cartItems);

        List<OrderItemRequest> result = cartService.getCartContents(customerId);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Get Cart Contents - cart is empty")
    public void getCartContentsTestCartEmpty() {
        Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
        cartItems.put(1, null);
        int customerId = 1;
        cartService.setCartItems(cartItems);

        List<OrderItemRequest> result = cartService.getCartContents(customerId);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Validate Car - happy flow")
    public void validatecarTestHappyFlow() {
        Car car = new Car("Audi", "A6", 50, CarCategory.SPORT, 10);
        car.setId(1);
        when(carService.findCarById(anyInt())).thenReturn(car);
        int quantity = 2;

        Car result = cartService.validateCar(car.getId(), quantity);

        assertEquals(car.getName(), result.getName());
        assertEquals(car.getDescription(), result.getDescription());
        assertEquals(car.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("Validate Car - negative quantity")
    public void validatecarTestNegativeQuantity() {
        Car car = new Car("Audi", "A6", 50, CarCategory.SPORT, 10);
        car.setId(1);
        when(carService.findCarById(anyInt())).thenReturn(car);
        int quantity = -1;

        NegativeQuantityException exception = assertThrows(NegativeQuantityException.class, () -> cartService.validateCar(car.getId(), quantity));
        assertEquals("The quantity cannot be zero or a negative number!", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Car - not in stock")
    public void validatecarTestNotInStock() {
        Car car = new Car("AUDI", "A6", 50, CarCategory.SPORT, 2);
        car.setId(1);
        when(carService.findCarById(anyInt())).thenReturn(car);
        int quantity = 4;

        CarNotInStockException exception = assertThrows(CarNotInStockException.class, () -> cartService.validateCar(car.getId(), quantity));
        assertEquals("Not enough stock available for Car with Id " + car.getId() + ".", exception.getMessage());
    }

}
