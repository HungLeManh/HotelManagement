package com.code.hotel_management.repository;

import com.code.hotel_management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}