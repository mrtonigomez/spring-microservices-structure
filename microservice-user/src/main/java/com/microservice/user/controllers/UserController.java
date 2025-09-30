package com.microservice.user.controllers;

import com.microservice.user.dto.get.UserGetDto;
import com.microservice.user.dto.post.UserPostDto;
import com.microservice.user.dto.request.UserRequestDto;
import com.microservice.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserGetDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // ✅ Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // ✅ Obtener un usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    // ✅ Crear usuario nuevo
    @PostMapping
    public ResponseEntity<UserPostDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserPostDto createdUser = userService.save(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // ✅ Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserPostDto> updateUser(@PathVariable Long id,
                                                  @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.update(id, userRequestDto));
    }

    // ✅ Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

