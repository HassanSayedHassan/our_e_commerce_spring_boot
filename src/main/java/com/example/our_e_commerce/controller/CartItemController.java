package com.example.our_e_commerce.controller;

import com.example.our_e_commerce.dto.UserDto;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.CartItem;
import com.example.our_e_commerce.model.User;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.service.cart.CartItemServices;
import com.example.our_e_commerce.service.cart.ICartItemServices;
import com.example.our_e_commerce.service.cart.ICartServices;
import com.example.our_e_commerce.service.user.IUserService;
import com.example.our_e_commerce.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart-item")
public class CartItemController {
    private final ICartItemServices cartItemServices;
    private final ICartServices cartServices;
    private final IUserService userService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCard(@RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartServices.initializeNewCart(user);
            cartItemServices.addCartItem(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeCartItem(@RequestParam Long cartId, @RequestParam Long productId) {
        try {
            cartItemServices.removeCartItem(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestParam Long cartId,
                                                      @RequestParam Long productId,
                                                      @RequestParam Integer quantity) {
        try {

            cartItemServices.updateCartItem(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
