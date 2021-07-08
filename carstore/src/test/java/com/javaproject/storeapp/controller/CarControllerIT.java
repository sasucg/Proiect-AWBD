package com.javaproject.storeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.storeapp.dto.CarRequest;
import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.entity.CarCategory;
import com.javaproject.storeapp.mapper.CarMapper;
import com.javaproject.storeapp.service.ImageService;
import com.javaproject.storeapp.service.CarService;
import com.javaproject.storeapp.service.ReviewService;
import com.javaproject.storeapp.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CarController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("mysql")
public class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("Create a new Car")
    @WithMockUser(roles = "ADMIN")
    public void createCarTest() throws Exception {
        CarRequest request = new CarRequest("BMW", "F30", 10000, 12, CarCategory.SPORT);

        when(carService.createCar(any())).thenReturn(new Car(1, "BMW", "F30", 10000, CarCategory.SPORT, 12));

        mockMvc.perform(post("/cars")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("admin2021")))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Get Car by Id - Not found")
    public void getcarByIdNotFoundTest() throws Exception {
        mockMvc.perform(get("/cars/{id}", 20))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notfound"));

    }

    @Test
    @DisplayName("Get Car by Id - Positive case")
    public void getcarByIdTest() throws Exception {
        Car car = new Car(1, "BMW", "F30", 10000, CarCategory.SPORT, 5);
        mockMvc.perform(get("/cars/{id}", "1"))//.param("car", String.valueOf(car)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("car", car))
                .andExpect(view().name("carDetails"));

    }
}
