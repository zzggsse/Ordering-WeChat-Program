package com.tea.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "商品不能为空")
    private Long productId;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    private String sugar;
    private String temp;
    private String cupSize;
}
