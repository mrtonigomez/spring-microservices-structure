package com.microservice_authenticate.client;

import com.microservice_authenticate.client.dto.get.UserGetDto;
import com.microservice_authenticate.client.dto.post.UserPostDto;
import com.microservice_authenticate.client.dto.request.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "msvc-user", url = "localhost:9002/api/users")
public interface UserClient {


    @PostMapping
    ResponseEntity<UserPostDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto);

    @GetMapping("/email/{email}")
    ResponseEntity<UserGetDto> getUserByEmail(@PathVariable String email);
}
