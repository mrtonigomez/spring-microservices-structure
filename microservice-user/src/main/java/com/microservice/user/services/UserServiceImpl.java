package com.microservice.user.services;

import com.microservice.user.dto.get.UserGetDto;
import com.microservice.user.dto.post.UserPostDto;
import com.microservice.user.dto.request.UserRequestDto;
import com.microservice.user.entities.User;
import com.microservice.user.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToGetDto(user);
    }

    @Override
    public UserGetDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return mapToGetDto(user);
    }

    @Override
    public UserPostDto update(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setEmail(userRequestDto.getEmail());
        user.setFullName(userRequestDto.getFullName());
        user.setPassword(userRequestDto.getPassword());

        User updatedUser = userRepository.save(user);
        return mapToPostDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserPostDto save(UserRequestDto userRequestDto) {
        User user = new User();
        user.setFullName(userRequestDto.getFullName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        User userSaved = userRepository.save(user);

        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setId(userSaved.getId());
        userPostDto.setEmail(userSaved.getEmail());
        userPostDto.setFullName(userSaved.getFullName());

        return userPostDto;
    }

    //Método auxiliar para mapear a DTO
    private UserGetDto mapToGetDto(User user) {
        UserGetDto dto = new UserGetDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPassword(user.getPassword());
        return dto;
    }

    private UserPostDto mapToPostDto(User user) {
        UserPostDto dto = new UserPostDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
