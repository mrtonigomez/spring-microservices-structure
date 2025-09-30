package com.microservice_authenticate.service;

import com.microservice_authenticate.client.UserClient;
import com.microservice_authenticate.client.dto.get.UserGetDto;
import com.microservice_authenticate.client.dto.post.UserPostDto;
import com.microservice_authenticate.client.dto.request.UserRequestDto;
import com.microservice_authenticate.dto.LoginUserDto;
import com.microservice_authenticate.dto.RegisterUserDto;
import com.microservice_authenticate.persistance.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserClient userClient;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserClient userClient
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userClient = userClient;
    }

    public ResponseEntity<UserPostDto> signup(RegisterUserDto input) {
        UserRequestDto user = new UserRequestDto();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userClient.createUser(user);
    }

    public UserGetDto authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userClient.getUserByEmail(input.getEmail()).getBody();
    }
}
