package com.microservice.user.controllers;

import com.microservice.user.dto.get.UserGetDto;
import com.microservice.user.dto.post.UserPostDto;
import com.microservice.user.dto.request.UserRequestDto;
import com.microservice.user.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    // READ ALL → GET /users
    @GetMapping
    public ResponseEntity<List<UserGetDto>> getAllUsers() {
        List<UserGetDto> users = userServiceImpl.findAll();
        return ResponseEntity.ok(users);
    }

    // READ BY ID → GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable Long id) {
        UserGetDto user = userServiceImpl.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<UserGetDto> getUserByEmail(@RequestParam String email) {
        UserGetDto user = userServiceImpl.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    // UPDATE → PUT /users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserPostDto> updateUser(@PathVariable Long id,
                                                  @RequestBody UserRequestDto userRequestDto) {
        UserPostDto updatedUser = userServiceImpl.update(id, userRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE → DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<UserPostDto> save(@RequestBody UserRequestDto userVO) {
        return ResponseEntity.ok(userServiceImpl.save(userVO));
    }
}
