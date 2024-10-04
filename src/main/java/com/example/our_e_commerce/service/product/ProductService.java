package com.example.our_e_commerce.service.product;

import com.example.our_e_commerce.dto.ImageDto;
import com.example.our_e_commerce.dto.ProductDto;
import com.example.our_e_commerce.exceptions.AlreadyExistException;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Category;
import com.example.our_e_commerce.model.Image;
import com.example.our_e_commerce.model.Product;
import com.example.our_e_commerce.repository.CategoryRepository;
import com.example.our_e_commerce.repository.ImageRepository;
import com.example.our_e_commerce.repository.ProductRepository;
import com.example.our_e_commerce.request.AddProductRequest;
import com.example.our_e_commerce.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //this injects (final and Private) dependencies
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        // check if category exists
        // IF Yes set it as new product

        //Else Save it as new Category
        // then save the product

        if(productExists(request.getName(),request.getBrand())){
            throw new AlreadyExistException("Product already exists with name: " + request.getName() + " and brand: " + request.getBrand());
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);

        return  productRepository.save(createProductHelper(request, category));
    }

    private boolean productExists(String name,String brand) {
        return productRepository.existsByNameAndBrand(name,brand);
    }


    private Product createProductHelper(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateProductHelper(existingProduct, request))
                /*productRepository::save is a method reference that
                 saves the updated product in the database, and returns the saved entity.*/
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }
    private Product updateProductHelper(Product existingProduct,UpdateProductRequest request) {
      existingProduct.setName(request.getName());
      existingProduct.setBrand(request.getBrand());
      existingProduct.setPrice(request.getPrice());
      existingProduct.setInventory(request.getInventory());
      existingProduct.setDescription(request.getDescription());

      Category category = categoryRepository.findByName(request.getCategory().getName());
      existingProduct.setCategory(category);

      return existingProduct;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        });
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String Brand, String name) {
        return productRepository.findByBrandAndName(Brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String Brand, String name) {
        return productRepository.countByBrandAndName(Brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
