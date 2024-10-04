package com.example.our_e_commerce.request;

import com.example.our_e_commerce.model.Category;
import com.example.our_e_commerce.model.Image;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    private Category category;


}
