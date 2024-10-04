package com.example.our_e_commerce.controller;


import com.example.our_e_commerce.dto.ImageDto;
import com.example.our_e_commerce.model.Image;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(
            @RequestParam Long productId, @RequestParam List<MultipartFile> files) {

        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return ResponseEntity.ok().body(new ApiResponse("UploadSuccess", imageDtos ));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), "UploadFailed"));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(
            @PathVariable Long imageId) throws SQLException {
           Image image=imageService.getImageById(imageId);
           ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
           return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())).
                   header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFileName()+"\"").body(resource);

    }


    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(
            @PathVariable Long imageId, @RequestParam MultipartFile file) {
        try {
            Image image=imageService.getImageById(imageId);
            if(image!=null){
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok().body(new ApiResponse("UpdateSuccess",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), "UpdateFailed"));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("UpdateFailed",null));
    }


    @DeleteMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image=imageService.getImageById(imageId);
            if(image!=null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok().body(new ApiResponse("Delete Success",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), "Delete Failed"));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed",null));
    }
}
