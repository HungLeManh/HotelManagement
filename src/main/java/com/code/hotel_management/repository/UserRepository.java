package com.code.hotel_management.repository;

import com.code.hotel_management.dto.request.UserRequestDTO;
import com.code.hotel_management.dto.response.UserDetailResponse;
import com.code.hotel_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
