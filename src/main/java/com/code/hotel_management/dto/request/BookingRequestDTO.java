package com.code.hotel_management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class BookingRequestDTO implements Serializable {
    @NotNull(message = "userid must be not null")
    private Long userId;

    @NotNull(message = "roomids must be not null")
    private List<Long> roomIds;

    @NotNull(message = "checkindate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date checkinDate;

    @NotNull(message = "checkoutdate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date checkoutDate;
}