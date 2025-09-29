package com.microservice_authenticate.client.dto.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {

    private Long id;
    private String fullName;
    private String email;
    private String password;

}
