package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.order.common.BusinessException;
import com.tea.order.common.OrderStatus;
import com.tea.order.common.Paged;
import com.tea.order.dto.OrderCreateRequest;
import com.tea.order.dto.OrderItemRequest;
import com.tea.order.entity.Order;
import com.tea.order.entity.OrderItem;
import com.tea.order.entity.Product;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.OrderItemMapper;
import com.tea.order.mapper.OrderMapper;
import com.tea.order.mapper.ProductMapper;
import com.tea.order.mapper.WechatUserMapper;
import com.tea.order.security.LoginUser;
import com.tea.order.vo.OrderItemVO;
import com.tea.order.vo.OrderVO;
import com.tea.order.websocket.OrderWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final WechatUserMapper wechatUserMapper;
    private final PointsService pointsService;
    private final OrderWebSocketHandler wsHandler;

    @Value("${app.pay.mock:true}")
    private boolean payMock;
    @Value("${app.order.auto-complete-hours:2}")
    private int autoCompleteHours;

    private static final long DEFAULT_STORE_ID = 1L;

    @Transactional
    public OrderVO create(OrderCreateRequest req, LoginUser user) {
        List<OrderItemRequest> items = req.getItems();
        if (items == null || items.isEmpty()) {
            throw new BusinessException("订单明细不能为空");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> toInsert = new java.util.ArrayList<>();
        for (OrderItemRequest item : items) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException("商品数量必须大于 0");
            }
            Product p = productMapper.selectById(item.getProductId());
            if (p == null) throw new BusinessException("商品不存在");
            if (p.getStatus() == null || p.getStatus() != 1) throw new BusinessException("商品「" + p.getName() + "」已下架");
            if (p.getSoldout() != null && p.getSoldout() == 1) throw new BusinessException("商品「" + p.getName() + "」已售罄");
            BigDecimal amount = p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(amount);
            toInsert.add(new OrderItem()
                    .setProductId(p.getId()).setName(p.getName()).setPrice(p.getPrice())
                    .setQuantity(item.getQuantity()).setAmount(amount)
                    .setSugar(item.getSugar()).setTemp(item.getTemp()).setCupSize(item.getCupSize()));
        }
        Order order = new Order()
                .setOrderNo(genOrderNo())
                .setStoreId(user.getStoreId() == null ? DEFAULT_STORE_ID : user.getStoreId())
                .setUserId(user.getId())
                .setPickupType(req.getPickupType() == null ? "自取" : req.getPickupType())
                .setStatus(OrderStatus.UNPAID.name())
                .setTotalAmount(total)
                .setRemark(req.getRemark())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);
        for (OrderItem oi : toInsert) {
            oi.setOrderId(order.getId());
            orderItemMapper.insert(oi);
        }
        return toVO(order);
    }

    @Transactional
    public OrderVO pay(Long orderId, LoginUser user) {
        Order o = require(orderId);
        checkOwner(o, user);
        if (!OrderStatus.UNPAID.name().equals(o.getStatus())) {
            throw new BusinessException("订单状态不允许支付");
        }
        doPaid(o);
        return toVO(o);
    }

    /** 模拟微信支付回调：未支付订单置为已支付（幂等）。 */
    @Transactional
    public OrderVO mockNotify(String orderNo) {
        Order o = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo));
        if (o == null) throw new BusinessException("订单不存在");
        if (OrderStatus.PAID.name().equals(o.getStatus())) {
            return toVO(o);
        }
        if (!OrderStatus.UNPAID.name().equals(o.getStatus())) {
            throw new BusinessException("订单状态不允许支付");
        }
        doPaid(o);
        return toVO(o);
    }

    private void doPaid(Order o) {
        o.setStatus(OrderStatus.PAID.name());
        o.setPaidAt(LocalDateTime.now());
        o.setPickupNo(generatePickupNo(o.getStoreId()));
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        // 增加销量
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, o.getId()));
        for (OrderItem it : items) {
            productMapper.update(null, new LambdaUpdateWrapper<Product>()
                    .eq(Product::getId, it.getProductId())
                    .setSql("sales = sales + " + Math.max(1, it.getQuantity())));
        }
        broadcast(o, "ORDER_PAID");
        log.info("订单已支付: {}", o.getOrderNo());
    }

    @Transactional
    public OrderVO accept(Long orderId, LoginUser admin) {
        Order o = require(orderId);
        requireStatus(o, OrderStatus.PAID);
        o.setStatus(OrderStatus.MAKING.name());
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_MAKING");
        return toVO(o);
    }

    @Transactional
    public OrderVO ready(Long orderId, LoginUser admin) {
        Order o = require(orderId);
        requireStatus(o, OrderStatus.MAKING);
        o.setStatus(OrderStatus.READY.name());
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_READY");
        log.info("叫号: 取餐号 {} 请取餐", o.getPickupNo());
        return toVO(o);
    }

    @Transactional
    public OrderVO done(Long orderId, LoginUser admin) {
        Order o = require(orderId);
        requireStatus(o, OrderStatus.READY);
        o.setStatus(OrderStatus.DONE.name());
        o.setDoneAt(LocalDateTime.now());
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        // 会员累计
        WechatUser w = wechatUserMapper.selectById(o.getUserId());
        if (w != null) {
            w.setTotalOrders((w.getTotalOrders() == null ? 0 : w.getTotalOrders()) + 1);
            w.setTotalAmount(w.getTotalAmount().add(o.getTotalAmount()));
            w.setUpdatedAt(LocalDateTime.now());
            wechatUserMapper.updateById(w);
        }
        broadcast(o, "ORDER_DONE");
        return toVO(o);
    }

    /** 顾客确认取餐：待取餐(READY) -> 已完成(DONE) */
    @Transactional
    public OrderVO receive(Long orderId, LoginUser user) {
        Order o = require(orderId);
        if (!java.util.Objects.equals(o.getUserId(), user.getId())) {
            throw new BusinessException(com.tea.order.common.ResultCode.FORBIDDEN);
        }
        requireStatus(o, OrderStatus.READY);
        complete(o, "ORDER_DONE");
        return toVO(o);
    }

    /** 定时任务：待取餐超过设定小时数未确认，自动完成 */
    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void autoCompleteReady() {
        LocalDateTime deadline = LocalDateTime.now().minusHours(autoCompleteHours);
        List<Order> list = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, OrderStatus.READY.name())
                .lt(Order::getUpdatedAt, deadline)
                .last("LIMIT 200"));
        for (Order o : list) {
            try {
                complete(o, "ORDER_DONE");
                log.info("订单 {} 超时 {} 小时未取餐，已自动完成", o.getOrderNo(), autoCompleteHours);
            } catch (Exception e) {
                log.warn("自动完成订单 {} 失败: {}", o.getOrderNo(), e.getMessage());
            }
        }
    }

    private void complete(Order o, String evt) {
        o.setStatus(OrderStatus.DONE.name());
        o.setDoneAt(LocalDateTime.now());
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        WechatUser w = wechatUserMapper.selectById(o.getUserId());
        if (w != null) {
            w.setTotalOrders((w.getTotalOrders() == null ? 0 : w.getTotalOrders()) + 1);
            w.setTotalAmount((w.getTotalAmount() == null ? BigDecimal.ZERO : w.getTotalAmount()).add(o.getTotalAmount()));
            pointsService.accrueConsumption(w, o.getTotalAmount());
            w.setUpdatedAt(LocalDateTime.now());
            wechatUserMapper.updateById(w);
        }
        broadcast(o, evt);
    }

    @Transactional
    public OrderVO cancel(Long orderId, LoginUser user, String reason) {
        Order o = require(orderId);
        checkOwner(o, user);
        if (!OrderStatus.UNPAID.name().equals(o.getStatus())
                && !OrderStatus.PAID.name().equals(o.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }
        o.setStatus(OrderStatus.CANCELED.name());
        o.setCancelReason(reason);
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_CANCELED");
        return toVO(o);
    }

    @Transactional
    public OrderVO refundApply(Long orderId, String reason) {
        Order o = require(orderId);
        String s = o.getStatus();
        if (!OrderStatus.PAID.name().equals(s) && !OrderStatus.MAKING.name().equals(s)) {
            throw new BusinessException("当前状态不可申请退款");
        }
        o.setStatus(OrderStatus.REFUNDING.name());
        o.setCancelReason(reason);
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_REFUNDING");
        return toVO(o);
    }

    @Transactional
    public OrderVO refundApprove(Long orderId, String reason) {
        Order o = require(orderId);
        requireStatus(o, OrderStatus.REFUNDING);
        o.setStatus(OrderStatus.REFUNDED.name());
        o.setRejectReason(reason);
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_REFUNDED");
        return toVO(o);
    }

    @Transactional
    public OrderVO reject(Long orderId, String reason) {
        Order o = require(orderId);
        requireStatus(o, OrderStatus.PAID);
        o.setStatus(OrderStatus.CANCELED.name());
        o.setRejectReason(reason);
        o.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(o);
        broadcast(o, "ORDER_REJECTED");
        return toVO(o);
    }

    public Paged<OrderVO> adminList(String status, long page, long size) {
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<Order>()
                .last(" ORDER BY (pickup_no IS NULL), CAST(pickup_no AS UNSIGNED) ASC, id DESC");
        if (status != null && !status.isBlank()) {
            List<String> list = java.util.Arrays.stream(status.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty())
                    .map(this::normalizeStatus).toList();
            if (!list.isEmpty()) {
                qw.in(Order::getStatus, list);
            }
        }
        return pageOrders(new Page<>(page, size), qw);
    }

    public Paged<OrderVO> mine(Long userId, String scope, long page, long size) {
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId);
        LocalDate today = LocalDate.now();
        if ("history".equalsIgnoreCase(scope)) {
            qw.lt(Order::getCreatedAt, today.atStartOfDay());
        } else {
            // 默认：当日订单
            qw.ge(Order::getCreatedAt, today.atStartOfDay());
        }
        qw.orderByDesc(Order::getCreatedAt);
        return pageOrders(new Page<>(page, size), qw);
    }

    public OrderVO detail(Long orderId) {
        return toVO(require(orderId));
    }

    private Paged<OrderVO> pageOrders(Page<Order> page, LambdaQueryWrapper<Order> qw) {
        Page<Order> p = orderMapper.selectPage(page, qw);
        List<OrderVO> vos = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new Paged<>(p.getTotal(), p.getCurrent(), p.getSize(), vos);
    }

    private OrderVO toVO(Order o) {
        if (o == null) return null;
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, o.getId()));
        List<OrderItemVO> itemVOs = items.stream().map(it -> {
            OrderItemVO vo = new OrderItemVO();
            vo.setProductId(it.getProductId()).setName(it.getName()).setPrice(it.getPrice())
                    .setQuantity(it.getQuantity()).setAmount(it.getAmount())
                    .setSugar(it.getSugar()).setTemp(it.getTemp()).setCupSize(it.getCupSize());
            return vo;
        }).collect(Collectors.toList());
        OrderVO vo = new OrderVO();
        vo.setId(o.getId()).setOrderNo(o.getOrderNo()).setStoreId(o.getStoreId())
                .setUserId(o.getUserId()).setPickupType(o.getPickupType())
                .setStatus(o.getStatus()).setStatusLabel(labelOf(o.getStatus()))
                .setTotalAmount(o.getTotalAmount()).setPickupNo(o.getPickupNo())
                .setRemark(o.getRemark()).setCancelReason(o.getCancelReason())
                .setRejectReason(o.getRejectReason()).setPaidAt(o.getPaidAt())
                .setDoneAt(o.getDoneAt()).setCreatedAt(o.getCreatedAt())
                .setUpdatedAt(o.getUpdatedAt()).setItems(itemVOs);
        return vo;
    }

    private Order require(Long id) {
        Order o = orderMapper.selectById(id);
        if (o == null) throw new BusinessException("订单不存在");
        return o;
    }

    private void requireStatus(Order o, OrderStatus expected) {
        if (!expected.name().equals(o.getStatus())) {
            throw new BusinessException(com.tea.order.common.ResultCode.CONFLICT);
        }
    }

    private void checkOwner(Order o, LoginUser user) {
        boolean admin = "ADMIN".equals(user.getType());
        if (!admin && !java.util.Objects.equals(o.getUserId(), user.getId())) {
            throw new BusinessException(com.tea.order.common.ResultCode.FORBIDDEN);
        }
    }

    private String normalizeStatus(String raw) {
        for (OrderStatus s : OrderStatus.values()) {
            if (s.name().equalsIgnoreCase(raw) || s.getLabel().equals(raw)) {
                return s.name();
            }
        }
        return raw;
    }

    private String labelOf(String status) {
        try {
            return OrderStatus.valueOf(status).getLabel();
        } catch (Exception e) {
            return status;
        }
    }

    private void broadcast(Order o, String type) {
        Map<String, Object> event = Map.of(
                "type", type,
                "orderNo", o.getOrderNo(),
                "status", o.getStatus(),
                "pickupNo", o.getPickupNo(),
                "storeId", o.getStoreId());
        wsHandler.broadcast(event);
    }

    private String genOrderNo() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "OD" + ts + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    private String generatePickupNo(Long storeId) {
        LocalDate today = LocalDate.now();
        long todayPaid = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getStoreId, storeId)
                .ge(Order::getPaidAt, today.atStartOfDay()))
                .longValue();
        return String.valueOf(todayPaid + 1);
    }
}

