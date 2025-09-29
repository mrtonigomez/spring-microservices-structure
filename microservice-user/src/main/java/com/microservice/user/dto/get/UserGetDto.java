package com.microservice.user.dto.get;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {

    private Long id;
    private String fullName;
    private String email;
    private String password;

}
