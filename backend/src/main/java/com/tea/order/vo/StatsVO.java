package com.tea.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsVO {
    private BigDecimal todayRevenue;
    private Long todayOrders;
    private Long pendingOrders;
    private BigDecimal avgOrder;
}
