package com.example.our_e_commerce.controller;

import com.example.our_e_commerce.dto.CartDto;
import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.service.cart.ICartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartController {
    private final ICartServices cartServices;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart=cartServices.getCartById(id);
            CartDto cartDto = cartServices.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("success",cartDto));
        }catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/clear/{id}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartServices.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("success",null));
        }catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/total-amount/{id}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id) {
        try {
            BigDecimal totalPrice=cartServices.getTotalAmount(id);
            return ResponseEntity.ok(new ApiResponse("success",totalPrice));
        }catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


}
