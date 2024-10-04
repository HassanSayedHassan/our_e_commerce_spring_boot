package com.example.our_e_commerce.repository;

import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
