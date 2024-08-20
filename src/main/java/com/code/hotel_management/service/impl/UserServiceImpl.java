package com.code.hotel_management.service.impl;


import com.code.hotel_management.dto.request.LoginRequestDTO;
import com.code.hotel_management.dto.request.UserRequestDTO;
import com.code.hotel_management.dto.response.UserDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public long saveUser(UserRequestDTO request) {
        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(request.getPassword())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .build();

        userRepository.save(user);

        log.info("user register success");

        return user.getUserID();
    }

    @Override
    public boolean login(LoginRequestDTO requestDTO) {
        User user = User.builder()
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword())
                .build();

        User user1 = userRepository.findByUsername(user.getUsername());
        if (user1.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(long userID, UserRequestDTO requestDTO) {
        User user = getUserById(userID);
        user.setName(requestDTO.getName());
        user.setPassword(requestDTO.getPassword());
        user.setAddress(requestDTO.getAddress());
        user.setPhone(requestDTO.getPhone());
        user.setEmail(requestDTO.getEmail());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        userRepository.save(user);

        log.info("user update success");
    }

    @Override
    public void deleteUser(long userID) {

    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.stream().map(user -> UserDetailResponse.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .dateOfBirth(user.getDateOfBirth())
                    .build())
                .toList();
    }

    /**
     * Get user by ID
     *
     * @param userId
     * @return
     */
    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
