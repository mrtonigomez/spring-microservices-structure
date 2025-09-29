package com.microservice.user.dto.post;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDto {

    private Long id;
    private String fullName;
    private String email;
    private String password;

}
