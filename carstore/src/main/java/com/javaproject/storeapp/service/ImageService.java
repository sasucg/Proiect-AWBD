package com.javaproject.storeapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(int carId, MultipartFile file);
}
