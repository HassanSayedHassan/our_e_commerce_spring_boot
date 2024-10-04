package com.example.our_e_commerce.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String fileName;
    private String downloadUrl;
}
