package com.code.hotel_management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class BookingDetailResponse implements Serializable {
    @NotNull(message = "bookingId must be not null")
    private Long bookingId;
    @NotBlank(message = "name must be not blank")
    private String name;

    @NotNull(message = "userid must be not null")
    private Long userId;

    @NotNull(message = "roomids must be not null")
    private List<Long> roomIds;

    @NotNull(message = "prices must be not null")
    private List<BigDecimal> prices;

    @NotNull(message = "checkindate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date checkinDate;

    @NotNull(message = "checkoutdate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date checkoutDate;

    @NotNull(message = "bookingDate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date bookingDate;

    @NotNull(message = "totalmoney must be not null")
    private BigDecimal totalMoney;
}
