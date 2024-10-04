package com.example.our_e_commerce.repository;

import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
