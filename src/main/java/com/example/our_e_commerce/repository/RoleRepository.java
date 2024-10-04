package com.example.our_e_commerce.repository;

import com.example.our_e_commerce.model.Role;
import com.example.our_e_commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String role);

    Role findByName(String roleUser);

}
