package com.microservice_authenticate.controller;

import com.microservice_authenticate.client.dto.post.UserPostDto;
import com.microservice_authenticate.dto.LoginResponse;
import com.microservice_authenticate.dto.LoginUserDto;
import com.microservice_authenticate.dto.RegisterUserDto;
import com.microservice_authenticate.entities.User;
import com.microservice_authenticate.service.AuthenticationService;
import com.microservice_authenticate.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserPostDto> register(@RequestBody RegisterUserDto registerUserDto) {

        return authenticationService.signup(registerUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

}
