package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.CarRequest;
import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import com.javaproject.storeapp.entity.Review;
import com.javaproject.storeapp.mapper.CarMapper;
import com.javaproject.storeapp.service.ImageService;
import com.javaproject.storeapp.service.CarService;
import com.javaproject.storeapp.service.ReviewService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/cars")
@CrossOrigin(origins = {"http://localhost:8080"})
@Api(value = "/cars",
        tags = "cars")
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;
    private final ImageService imageService;
    private final ReviewService reviewService;

    public CarController(CarService carService, CarMapper carMapper, ImageService imageService, ReviewService reviewService) {
        this.carService = carService;
        this.carMapper = carMapper;
        this.imageService = imageService;
        this.reviewService = reviewService;
    }

    @GetMapping("/new")
    public String newCar(Model model) {
        List<CarCategory> categoriesAll = Arrays.asList(CarCategory.values());
        model.addAttribute("carRequest", new CarRequest());
        model.addAttribute("categoriesAll", categoriesAll);
        return "addCar";
    }

    @PostMapping
    public String addCar(@Valid
                             @RequestBody
                             @ModelAttribute
                                     CarRequest carRequest,
                             BindingResult bindingResult,
                             @RequestParam("imagefile") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "addCar";
        }
        Car car = carMapper.carRequestToCar(carRequest);
        Car createdCar = carService.createCar(car);
        imageService.saveImageFile(createdCar.getId(), file);
        return "redirect:/cars/";
    }

    @GetMapping("{id}")
    public String getCar(@PathVariable int id, Model model) {
        Car carFound = carService.findCarById(id);
        List<Review> reviewsFound = reviewService.getReviewsForcar(id);
        model.addAttribute("reviews", reviewsFound);
        model.addAttribute("car", carFound);
        return "carDetails";
    }

    @GetMapping()
    public String getAllCars(
            @RequestParam(required = false)
                    String category,
            @RequestParam(required = false)
                    String name,
            @RequestParam(required = false)
                    boolean descending,
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<Car> carPage = carService.getCarsBy(category, name, descending, PageRequest.of(currentPage - 1, pageSize));//, descending ? Sort.by("price").descending() : Sort.by("price").ascending()));
        model.addAttribute("carPage", carPage);
        int totalPages = carPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "cars";
    }

}
