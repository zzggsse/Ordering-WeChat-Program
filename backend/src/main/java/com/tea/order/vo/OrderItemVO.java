package com.tea.order.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OrderItemVO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
    private String sugar;
    private String temp;
    private String cupSize;
}
