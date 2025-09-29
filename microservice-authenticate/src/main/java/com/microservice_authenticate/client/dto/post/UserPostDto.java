package com.microservice_authenticate.client.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDto {

    private Long id;
    private String fullName;
    private String email;
    private String password;

}
