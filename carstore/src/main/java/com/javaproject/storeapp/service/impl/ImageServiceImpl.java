package com.javaproject.storeapp.service.impl;

import com.javaproject.storeapp.entity.Car;
import com.javaproject.storeapp.repository.CarRepository;
import com.javaproject.storeapp.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    private final CarRepository carRepository;

    public ImageServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(int carId, MultipartFile file) {
        try {
            Car car = carRepository.findCarById(carId);
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }
            car.setImage(byteObjects);
            carRepository.save(car);
        } catch (IOException e) {
        }
    }
}
