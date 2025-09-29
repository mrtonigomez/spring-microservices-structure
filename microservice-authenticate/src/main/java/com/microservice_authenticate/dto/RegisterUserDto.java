package com.microservice_authenticate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;
}
