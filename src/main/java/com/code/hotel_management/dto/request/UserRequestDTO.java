package com.code.hotel_management.dto.request;

import com.code.hotel_management.util.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "name must be not blank") // Khong cho phep gia tri blank
    private String name;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;

    @NotNull(message = "address must be not null")
    private String address;

    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @Email(message = "email invalid format") // Chi chap nhan nhung gia tri dung dinh dang email
    private String email;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;


    public UserRequestDTO(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

}