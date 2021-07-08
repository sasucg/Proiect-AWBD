package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.service.CarService;
import com.javaproject.storeapp.service.impl.BankAccountServiceImpl;
import com.javaproject.storeapp.service.impl.CartServiceImpl;
import com.javaproject.storeapp.service.impl.OrderServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderServiceImpl orderService;
    private final CartServiceImpl cartService;
    private final CarService carService;
    private final BankAccountServiceImpl bankAccountService;

    public CartController(OrderServiceImpl orderService, CartServiceImpl cartService, CarService carService, BankAccountServiceImpl bankAccountService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.carService = carService;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/add")
    public String addcarToCart(@RequestParam int carId,
                                   @RequestParam int quantity, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Car car = cartService.validateCar(carId, quantity);
        OrderItemRequest item = new OrderItemRequest(car, quantity, car.getPrice());
        cartService.addcarToCart(user, item);
        return "redirect:/cart/";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteItemFromCart(@RequestParam int carId, Principal principal, Model model) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(user);
        List<OrderItemRequest> items = cartService.getCartContents(user.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart);
        cartService.deleteItemFromCart(cart, user.getId(), carId);
        return "cart";
    }

    @GetMapping()
    public String getCartContents(Principal principal, Model model) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(user);
        List<OrderItemRequest> items = cartService.getCartContents(user.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart != null ? cart : new Cart(0));
        model.addAttribute("accounts", bankAccountService.getBankAccountsForUser(user));
        return "cart";
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(@RequestParam int accountId,
                                 Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Optional<Order> order = orderService.createOrder(user, cartService.getCartItems().get(user.getId()), accountId);
        ModelAndView modelAndView;
        if (order.isPresent()) {
            modelAndView = new ModelAndView("orders");
            modelAndView.addObject("orders", orderService.getOrdersByUser(user));
        } else {
            modelAndView = new ModelAndView("cart");
            Cart cart = cartService.findCartByUser(user);
            List<OrderItemRequest> items = cartService.getCartContents(user.getId());
            modelAndView.addObject("items", items);
            modelAndView.addObject("cart", cart != null ? cart : new Cart(0));
            modelAndView.addObject("accounts", bankAccountService.getBankAccountsForUser(user));
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateQuantity(@RequestParam int carId,
                                       @RequestParam int quantity,
                                       Principal principal) {

        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        OrderItemRequest itemRequest = cartService.getItemBycarId(carId, user.getId());
        Cart cart = cartService.findCartByUser(user);
        Car car = carService.findCarById(carId);

        ModelAndView model = new ModelAndView("cart");
        if (car.getStock() < quantity || quantity <= 0) {
            model.addObject("cart", cart != null ? cart : new Cart(0));
            model.addObject("noStock", "Car with id" + car.getId() + " not in stock");
            model.addObject("accounts", bankAccountService.getBankAccountsForUser(user));
            return model;
        }
        model.addObject("noStock", "NO");
        int oldQuantity = cartService.updateItemQuantity(user.getId(), itemRequest, quantity);
        double newValue = cart.getTotalAmount() - oldQuantity * itemRequest.getPrice() + quantity * itemRequest.getPrice();

        cartService.updateCartAmount(cart.getId(), newValue);
        cart = cartService.findCartByUser(user);

        model.addObject("cart", cart != null ? cart : new Cart(0));
        model.addObject("accounts", bankAccountService.getBankAccountsForUser(user));
        return model;
    }
}


