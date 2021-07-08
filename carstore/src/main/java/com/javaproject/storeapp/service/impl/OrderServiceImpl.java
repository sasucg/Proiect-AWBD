package com.javaproject.storeapp.service.impl;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.*;
import com.javaproject.storeapp.exception.BankAccountNotBelongingToCustomer;
import com.javaproject.storeapp.exception.CartIsEmptyException;
import com.javaproject.storeapp.exception.InsufficientFundsException;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.OrderItemRepository;
import com.javaproject.storeapp.repository.OrderRepository;
import com.javaproject.storeapp.service.BankAccountService;
import com.javaproject.storeapp.service.CartService;
import com.javaproject.storeapp.service.OrderService;
import com.javaproject.storeapp.service.CarService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BankAccountService bankAccountService;
    private final OrderItemRepository orderItemRepository;
    private final CarService carService;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, BankAccountService bankAccountService, OrderItemRepository orderItemRepository, CarService carService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.bankAccountService = bankAccountService;
        this.orderItemRepository = orderItemRepository;
        this.carService = carService;
        this.cartService = cartService;
    }

    @Override
    public Order findOrderById(int id) {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findOrderById(id));
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new ResourceNotFoundException("Order with Id " + id + " not found.");
        }
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findOrdersByUser(user);
    }

    @Transactional
    @Override
    public Optional<Order> createOrder(User user, List<OrderItemRequest> orderItemRequests, int accountId) {

        BankAccount bankAccount = validateBankAccount(user.getId(), accountId);

        Cart cart = cartService.findCartByUser(user);
        if (cart == null)
            throw new ResourceNotFoundException("Cart for customer with Id " + user.getId() + " not found.");
        if (cart.getTotalAmount() == 0)
            throw new CartIsEmptyException(user.getId());

        // Creating order object
        Order order = new Order();
        order.setUser(user);
        order.setAccount(bankAccount);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest item : orderItemRequests) {
            Car car = carService.findCarById(item.getcar().getId());
            int stock = car.getStock();
            // if there is available stock for the car, we add to list of order items
            if (stock >= item.getQuantity()) {
                carService.updateStock(car.getId(), stock - item.getQuantity());
                total += car.getPrice() * item.getQuantity();
                orderItems.add(new OrderItem(item.getQuantity(), item.getPrice(), car));
            }
        }

        if (total == 0)
            return Optional.empty();
        // check if there is enough money in the account to pay the order
        boolean result = checkBalanceForOrder(bankAccount, total);
        order.setTotalAmount(total);
        order.setDatePlaced(LocalDate.now());

        // Saving entities in database
        orderRepository.save(order);
        orderItems.forEach(item -> {
            item.setOrder(order);
            orderItemRepository.save(item);
        });

        // withdraw money from bank account
        bankAccountService.withdrawMoneyFromAccount(bankAccount.getId(), bankAccount.getBalance() - total);

        cartService.resetCart(cart);
        return Optional.of(order);
    }

    @Override
    public boolean checkBalanceForOrder(BankAccount bankAccount, double total) {
        if (bankAccount.getBalance() < total)
            throw new InsufficientFundsException(bankAccount.getId());
        else return true;
    }

    @Override
    public BankAccount validateBankAccount(int userId, int accountId) {
        BankAccount bankAccount = bankAccountService.findBankAccountById(accountId);
        if (userId != bankAccount.getUser().getId())
            throw new BankAccountNotBelongingToCustomer(accountId, userId);
        return bankAccount;
    }
}
