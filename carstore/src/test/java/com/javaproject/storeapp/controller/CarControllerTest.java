package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.mapper.CarMapper;
import com.javaproject.storeapp.service.ImageService;
import com.javaproject.storeapp.service.CarService;
import com.javaproject.storeapp.service.ReviewService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CarControllerTest {

    @Mock
    private Model model;

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private ReviewService reviewService;

    private CarController carController;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        carController = new CarController(carService, carMapper, imageService, reviewService);
    }

    @Captor
    ArgumentCaptor<Car> argumentCaptor;

    @Test
    @DisplayName("Get Car by Id Test")
    public void getcarByIdTest() {
        int id = 1;
        Car carTest = new Car();
        carTest.setId(id);

        when(carService.findCarById(id)).thenReturn(carTest);

        String viewName = carController.getCar(id, model);
        Assert.assertEquals("carDetails", viewName);
        verify(carService, times(1)).findCarById(id);

        verify(model, times(1))
                .addAttribute(eq("car"), argumentCaptor.capture());

        Car carArg = argumentCaptor.getValue();
        Assert.assertEquals(carArg.getId(), carTest.getId());
    }
}

