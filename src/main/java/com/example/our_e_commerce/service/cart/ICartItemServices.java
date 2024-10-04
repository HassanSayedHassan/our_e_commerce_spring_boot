package com.example.our_e_commerce.service.cart;

import com.example.our_e_commerce.model.CartItem;

public interface ICartItemServices {

    void addCartItem(Long cartId, Long productId, int quantity);

    void removeCartItem(Long cartId, Long productId);

    void updateCartItem(Long cartId, Long productId, int quantity);


    CartItem getCartItem(Long cartId, Long productId);
}
