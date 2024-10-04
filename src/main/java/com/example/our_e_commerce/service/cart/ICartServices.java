package com.example.our_e_commerce.service.cart;

import com.example.our_e_commerce.dto.CartDto;
import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.User;

import java.math.BigDecimal;

public interface ICartServices {

    Cart getCartById(Long id);

    void clearCart(Long cartId);
    BigDecimal getTotalAmount(Long cartId);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
    CartDto convertToDto(Cart cart);
}
