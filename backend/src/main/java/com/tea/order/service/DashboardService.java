package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.OrderStatus;
import com.tea.order.entity.Order;
import com.tea.order.mapper.OrderMapper;
import com.tea.order.vo.StatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderMapper orderMapper;

    private static final Set<String> PAID_SET = Set.of(
            OrderStatus.PAID.name(), OrderStatus.MAKING.name(),
            OrderStatus.READY.name(), OrderStatus.DONE.name(), OrderStatus.REFUNDING.name());

    public StatsVO stats() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        List<Order> todayPaid = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, todayStart)
                .in(Order::getStatus, PAID_SET));
        BigDecimal revenue = todayPaid.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long pending = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, OrderStatus.PAID.name()));
        long count = todayPaid.size();
        BigDecimal avg = count == 0 ? BigDecimal.ZERO
                : revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        return new StatsVO(revenue, count, pending, avg);
    }
}
