package com.microservice.user.services;

import com.microservice.user.dto.get.UserGetDto;
import com.microservice.user.dto.post.UserPostDto;
import com.microservice.user.dto.request.UserRequestDto;

import java.util.List;

public interface UserService {

    List<UserGetDto> findAll();                      // Read all
    UserGetDto findById(Long id);
    UserGetDto findByEmail(String id);                    // Read by id
    UserPostDto update(Long id, UserRequestDto userRequestDto); // Update
    void delete(Long id);
    UserPostDto save (UserRequestDto user);
}
