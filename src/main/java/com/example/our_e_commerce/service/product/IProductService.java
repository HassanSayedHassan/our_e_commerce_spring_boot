package com.example.our_e_commerce.service.product;

import com.example.our_e_commerce.dto.ProductDto;
import com.example.our_e_commerce.model.Product;
import com.example.our_e_commerce.request.AddProductRequest;
import com.example.our_e_commerce.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);

    Product updateProduct(UpdateProductRequest request, Long productId);

    void deleteProductById(Long id);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String Brand,String name);
    Long countProductsByBrandAndName(String Brand,String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
