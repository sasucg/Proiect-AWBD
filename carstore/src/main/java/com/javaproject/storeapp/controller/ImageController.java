package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.service.CarService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {
    private final CarService carService;

    public ImageController(@Autowired CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String returnHomePage() {
        return "home";
    }

    @GetMapping("cars/getImage/{id}")
    public void downloadImage(@PathVariable int id, HttpServletResponse response) throws IOException {
        Car car = carService.findCarById(id);

        if (car.getImage() != null) {
            byte[] byteArray = new byte[car.getImage().length];
            int i = 0;
            for (Byte wrappedByte : car.getImage()) {
                byteArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
