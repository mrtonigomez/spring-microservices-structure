package com.microservice_authenticate.client;

import com.microservice_authenticate.client.dto.get.UserGetDto;
import com.microservice_authenticate.client.dto.post.UserPostDto;
import com.microservice_authenticate.client.dto.request.UserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msvc-user", url = "localhost:9002/api/users")
public interface UserClient {


    @PostMapping()
    ResponseEntity<UserPostDto> save(@RequestBody UserRequestDto userVO);

    @GetMapping("/find-by-email")
    ResponseEntity<UserGetDto> findByEmail(@RequestParam String email);
}
