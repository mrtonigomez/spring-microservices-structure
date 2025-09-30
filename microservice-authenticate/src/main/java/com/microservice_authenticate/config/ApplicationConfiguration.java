package com.microservice_authenticate.config;

import com.microservice_authenticate.client.UserClient;
import com.microservice_authenticate.client.dto.get.UserGetDto;
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

import java.util.ArrayList;

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
                ResponseEntity<UserGetDto> userResponse = userClient.getUserByEmail(username);

                if (userResponse == null || userResponse.getBody() == null) {
                    throw new UsernameNotFoundException("User not found with email: " + username);
                }

                UserGetDto user = userResponse.getBody();

                if (user.getEmail() == null || user.getPassword() == null) {
                    throw new UsernameNotFoundException("Invalid user data for email: " + username);
                }

                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities(new ArrayList<>())
                        .build();

            } catch (FeignException.NotFound e) {
                throw new UsernameNotFoundException("User not found with email: " + username, e);
            } catch (FeignException e) {
                throw new RuntimeException("Error calling User Service", e);
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
