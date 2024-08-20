package com.code.hotel_management.service;


import com.code.hotel_management.dto.request.LoginRequestDTO;
import com.code.hotel_management.dto.request.UserRequestDTO;
import com.code.hotel_management.dto.response.UserDetailResponse;

import java.util.List;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO);

    boolean login(LoginRequestDTO requestDTO);

    void updateUser(long userID, UserRequestDTO requestDTO);

    void deleteUser(long userID);

    UserDetailResponse getUser(long userID);

    List<UserDetailResponse> getAllUsers(int pageNo, int pageSize);
}
