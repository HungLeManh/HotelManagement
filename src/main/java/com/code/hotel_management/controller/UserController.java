package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.LoginRequestDTO;
import com.code.hotel_management.dto.request.UserRequestDTO;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.dto.response.UserDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // Register
    @PostMapping(value = "/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Request add user, {}", user.getUsername());

        try {
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "add user success", userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    // Login
    @PostMapping(value = "/login")
    public ResponseData<String> login(@Valid @RequestBody LoginRequestDTO user) {
        log.info("Request login, {}", user.getUsername());

        try {
            if(userService.login(user)){
                return new ResponseData<>(HttpStatus.CREATED.value(), "login success", user.getUsername());
            }else return new ResponseData<>(HttpStatus.CREATED.value(), "incorrect password or userName");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Login fail");
        }
    }

    // update user
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO user) {
        log.info("Request update userId={}", userId);

        try {
            userService.updateUser(userId, user);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "user update success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@Min(1) @PathVariable int userId, @RequestParam boolean status) {
        log.info("Request change status, userId={}", userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User's status changed successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        log.info("Request delete userId={}", userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
    }

    // get user detail
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable @Min(1) long userId) {
        log.info("Request get user detail, userId={}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseData<List<UserDetailResponse>> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                         @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        log.info("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsers(pageNo, pageSize));
    }
}
