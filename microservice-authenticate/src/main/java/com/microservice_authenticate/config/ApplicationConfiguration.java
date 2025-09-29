package com.microservice_authenticate.config;

import com.microservice_authenticate.client.UserClient;
import com.microservice_authenticate.client.dto.get.UserGetDto;
import com.microservice_authenticate.persistance.UserRepository;
import feign.FeignException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserClient userClient;

    public ApplicationConfiguration(UserClient userClient) {
        this.userClient = userClient;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            try {
                ResponseEntity<UserGetDto> userResponse = userClient.findByEmail(username); // llama al microservicio
                UserGetDto user = userResponse.getBody();

                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword()) // contraseña codificada
                        .build();
            } catch (FeignException.NotFound e) {
                throw new UsernameNotFoundException("User not found");
            }
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
