package com.example.our_e_commerce.data;

import com.example.our_e_commerce.model.Role;
import com.example.our_e_commerce.model.User;
import com.example.our_e_commerce.repository.RoleRepository;
import com.example.our_e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRolesIfNotExists(roles);
        createDefaultUsersIfNotExists();
        createDefaultAdminsIfNotExists();
    }

    private void createDefaultUsersIfNotExists() {
        Role role = roleRepository.findByName("ROLE_USER");
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (!userRepository.existsByEmail(defaultEmail)) {
                User user = new User();
                user.setFirstName("The User");
                user.setLastName("User" + i);
                user.setEmail(defaultEmail);
                user.setRoles(Set.of(role));
                user.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(user);
            }
        }
    }

    private void createDefaultAdminsIfNotExists() {
        Role role = roleRepository.findByName("ROLE_ADMIN");
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (!userRepository.existsByEmail(defaultEmail)) {
                User user = new User();
                user.setFirstName("The admin");
                user.setLastName("admin" + i);
                user.setEmail(defaultEmail);
                user.setRoles(Set.of(role));
                user.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(user);
            }
        }
    }

    private void createDefaultRolesIfNotExists(Set<String> roles) {
        roles.stream().filter(
                role -> !roleRepository.existsByName(role)
        ).map(Role::new).forEach(roleRepository::save);
    }
}
