package com.example.our_e_commerce.service.image;

import com.example.our_e_commerce.dto.ImageDto;
import com.example.our_e_commerce.model.Image;
import com.example.our_e_commerce.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);

    List<ImageDto> saveImage(List<MultipartFile>  files, Long productId);
    void updateImage(MultipartFile file, Long imageId);

    List<Image> getAllImages();
}
