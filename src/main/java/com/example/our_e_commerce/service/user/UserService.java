package com.example.our_e_commerce.service.user;


import com.example.our_e_commerce.dto.UserDto;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.User;
import com.example.our_e_commerce.repository.UserRepository;
import com.example.our_e_commerce.request.CreateUserRequest;
import com.example.our_e_commerce.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder  passwordEncoder;



    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req ->{
                  User newUser = new User();
                  newUser.setFirstName(req.getFirstName());
                  newUser.setLastName(req.getLastName());
                  newUser.setEmail(req.getEmail());
                  newUser.setPassword(passwordEncoder.encode(req.getPassword()));
                  return userRepository.save(newUser);
                }).map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User already exists with email: " + request.getEmail()));

    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            return userRepository.save(user);
        }).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        });
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
