package com.amanboora.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto implements Serializable {

    private String id;
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
}
