package com.microservice.user.services;

import com.microservice.user.dto.get.UserGetDto;
import com.microservice.user.dto.post.UserPostDto;
import com.microservice.user.dto.request.UserRequestDto;
import com.microservice.user.entities.User;
import com.microservice.user.exception.DuplicateEmailException;
import com.microservice.user.exception.InvalidDataException;
import com.microservice.user.exception.UserNotFoundException;
import com.microservice.user.persistance.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserGetDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserGetDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToGetDto(user);
    }

    @Override
    public UserGetDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToGetDto(user);
    }

    @Override
    public UserPostDto update(Long id, UserRequestDto userRequestDto) {
        validateUserRequest(userRequestDto);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already in use: " + userRequestDto.getEmail());
            }
            user.setEmail(userRequestDto.getEmail());
        }

        user.setFullName(userRequestDto.getFullName());

        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            user.setPassword(userRequestDto.getPassword());
        }

        User updatedUser = userRepository.save(user);
        return mapToPostDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserPostDto save(UserRequestDto userRequestDto) {
        validateUserRequest(userRequestDto);

        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already in use: " + userRequestDto.getEmail());
        }

        User user = new User();
        user.setFullName(userRequestDto.getFullName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());

        User userSaved = userRepository.save(user);

        return mapToPostDto(userSaved);
    }

    // ✅ Validaciones básicas
    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException("Invalid email format");
        }
    }

    // ✅ Métodos auxiliares de mapeo
    private UserGetDto mapToGetDto(User user) {
        UserGetDto dto = new UserGetDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPassword(user.getPassword());
        return dto; // 🚫 Nunca devolvemos la password
    }

    private UserPostDto mapToPostDto(User user) {
        UserPostDto dto = new UserPostDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        return dto; // 🚫 Nunca devolvemos la password
    }
}

