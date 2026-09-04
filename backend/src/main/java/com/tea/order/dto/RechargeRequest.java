package com.tea.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeRequest {
    @NotNull(message = "充值金额不能为空")
    private BigDecimal amount;
}
