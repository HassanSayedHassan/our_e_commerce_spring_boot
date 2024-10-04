package com.example.our_e_commerce.service.image;

import com.example.our_e_commerce.dto.ImageDto;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Image;
import com.example.our_e_commerce.model.Product;
import com.example.our_e_commerce.repository.ImageRepository;
import com.example.our_e_commerce.repository.ProductRepository;
import com.example.our_e_commerce.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;



    @Override
    @Transactional

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image not found with id: " + id)
        );
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found with id: " + id);
        });

    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product=productService.getProductById(productId);
        List<ImageDto> savedImagesDto=new ArrayList<>();
        for(MultipartFile file:files){
            try {
                Image image=new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl="/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+ image.getId();
                image.setDownloadUrl(downloadUrl);

                Image saveImage = imageRepository.save(image);
                saveImage.setDownloadUrl(buildDownloadUrl + saveImage.getId());
                imageRepository.save(image);

                ImageDto imageDto=new ImageDto();
                imageDto.setImageId(saveImage.getId());
                imageDto.setFileName(saveImage.getFileName());
                imageDto.setDownloadUrl(saveImage.getDownloadUrl());
                savedImagesDto.add(imageDto);

            }catch (IOException|SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }


        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image=getImageById(imageId);
       try {
           image.setFileName(file.getOriginalFilename());
           image.setFileType(file.getContentType());
           image.setImage(new SerialBlob(file.getBytes()));
           imageRepository.save(image);
       }catch (IOException |SQLException e){
           throw new RuntimeException(e.getMessage());
       }
    }

    @Override
    public List<Image> getAllImages() {
       return imageRepository.findAll();
    }
}
