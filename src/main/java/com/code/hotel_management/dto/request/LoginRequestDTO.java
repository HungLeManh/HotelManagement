package com.code.hotel_management.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class LoginRequestDTO implements Serializable {


    @NotNull(message = "userName must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;


    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
