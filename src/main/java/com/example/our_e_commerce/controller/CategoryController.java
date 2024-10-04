package com.example.our_e_commerce.controller;

import com.example.our_e_commerce.model.Category;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.service.category.ICategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryServices categoryServices;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
       try {
           List<Category> categories = categoryServices.getAllCategories();
           return ResponseEntity.ok(new ApiResponse("Success", categories));
       }
       catch (Exception e){
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
       }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
       try {
           Category newCategory = categoryServices.addCategory(category);
           return ResponseEntity.ok(new ApiResponse("Success", newCategory));
       }catch (Exception e){
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
       }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryServices.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category updatedCategory = categoryServices.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Success", updatedCategory));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id){
        try {
            categoryServices.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Success", "Category deleted successfully"));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
