package com.code.hotel_management.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class PromotionRequestDTO implements Serializable {
    @NotNull(message = "promotionName must be not null")
    private String promotionName;

    @NotNull(message = "description must be not null")
    private String description;

    @NotNull(message = "discountRate must be not null")
    private BigDecimal discountRate;

    @NotNull(message = "startDate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "endDate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
