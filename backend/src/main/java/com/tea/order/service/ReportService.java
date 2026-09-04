package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.OrderStatus;
import com.tea.order.entity.Order;
import com.tea.order.entity.OrderItem;
import com.tea.order.entity.Product;
import com.tea.order.mapper.OrderItemMapper;
import com.tea.order.mapper.OrderMapper;
import com.tea.order.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;

    private static final Set<String> PAID_SET = Set.of(
            OrderStatus.PAID.name(), OrderStatus.MAKING.name(),
            OrderStatus.READY.name(), OrderStatus.DONE.name(), OrderStatus.REFUNDING.name());

    /** 按日销售趋势：{date, count, amount} */
    public List<Map<String, Object>> salesByDay(LocalDate start, LocalDate end) {
        LocalDate s = start == null ? LocalDate.now().minusDays(30) : start;
        LocalDate e = end == null ? LocalDate.now() : end;
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, s.atStartOfDay())
                .lt(Order::getCreatedAt, e.plusDays(1).atStartOfDay())
                .in(Order::getStatus, PAID_SET));
        Map<LocalDate, long[]> byDate = orders.stream().collect(Collectors.groupingBy(
                o -> o.getCreatedAt().toLocalDate(),
                LinkedHashMap::new,
                Collectors.collectingAndThen(Collectors.toList(), list -> {
                    long count = list.size();
                    BigDecimal amount = list.stream()
                            .map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new long[]{count, amount.setScale(2, java.math.RoundingMode.HALF_UP).longValue()};
                })));
        List<Map<String, Object>> rows = new ArrayList<>();
        LocalDate d = s;
        while (!d.isAfter(e)) {
            long[] v = byDate.getOrDefault(d, new long[]{0, 0});
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("date", d.toString());
            row.put("count", v[0]);
            row.put("amount", v[1]);
            rows.add(row);
            d = d.plusDays(1);
        }
        return rows;
    }

    /** 商品销量排行 */
    public List<Map<String, Object>> productSales(int limit) {
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .orderByDesc(Product::getSales)
                .last("LIMIT " + Math.max(1, Math.min(limit, 100))));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Product p : products) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", p.getId());
            row.put("name", p.getName());
            row.put("category", p.getCategory());
            row.put("sales", p.getSales());
            row.put("amount", BigDecimal.ZERO);
            rows.add(row);
        }
        return rows;
    }

    /** 高峰时段分布（按小时） */
    public List<Map<String, Object>> timeSlots(LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, d.atStartOfDay())
                .lt(Order::getCreatedAt, d.plusDays(1).atStartOfDay())
                .in(Order::getStatus, PAID_SET));
        Map<Integer, Long> byHour = orders.stream().collect(Collectors.groupingBy(
                o -> o.getCreatedAt().getHour(), Collectors.counting()));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("hour", String.format("%02d:00", h));
            row.put("count", byHour.getOrDefault(h, 0L));
            rows.add(row);
        }
        return rows;
    }
}
