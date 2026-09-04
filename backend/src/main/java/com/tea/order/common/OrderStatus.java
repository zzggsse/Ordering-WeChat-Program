package com.tea.order.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    UNPAID("待支付"),
    PAID("待接单"),
    MAKING("制作中"),
    READY("待取餐"),
    DONE("已完成"),
    CANCELED("已取消"),
    REFUNDING("退款中"),
    REFUNDED("已退款");
    private final String label;
}
