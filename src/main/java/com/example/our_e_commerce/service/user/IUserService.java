package com.example.our_e_commerce.service.user;

import com.example.our_e_commerce.dto.UserDto;
import com.example.our_e_commerce.model.User;
import com.example.our_e_commerce.request.CreateUserRequest;
import com.example.our_e_commerce.request.UpdateUserRequest;

public interface IUserService {


    User getUserById(Long userId);

    UserDto createUser(CreateUserRequest request);

    User updateUser(Long userId, UpdateUserRequest request);

    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
