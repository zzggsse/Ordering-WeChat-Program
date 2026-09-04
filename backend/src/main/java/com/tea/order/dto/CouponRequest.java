package com.tea.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponRequest {
    @NotBlank(message = "券名称不能为空")
    private String name;
    private String type = "FULL_REDUCTION";
    private BigDecimal threshold;
    private BigDecimal discount;
    private Integer pointsCost;
    private Integer oncePerUser;
    private Long total;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status = 1;
}
