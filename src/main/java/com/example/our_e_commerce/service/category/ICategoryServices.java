package com.example.our_e_commerce.service.category;

import com.example.our_e_commerce.model.Category;

import java.util.List;

public interface ICategoryServices {

    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();

    Category addCategory(Category category);
    Category updateCategory(Category category,Long id);
    void deleteCategoryById(Long id);

}
