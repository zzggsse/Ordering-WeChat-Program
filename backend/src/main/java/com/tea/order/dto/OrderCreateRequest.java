package com.tea.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private String pickupType = "自取";
    private String remark;
    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderItemRequest> items;
}
