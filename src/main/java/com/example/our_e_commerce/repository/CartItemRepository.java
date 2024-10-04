package com.example.our_e_commerce.repository;

import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
