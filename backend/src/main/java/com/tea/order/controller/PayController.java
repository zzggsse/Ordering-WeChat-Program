package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.service.OrderService;
import com.tea.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayController {

    private final OrderService orderService;

    /** 模拟微信支付回调：orderNo 对应的待支付订单置为已支付 */
    @PostMapping("/mock-notify")
    public ApiResponse<OrderVO> mockNotify(@RequestBody Map<String, Object> body) {
        String orderNo = String.valueOf(body.get("orderNo"));
        return ApiResponse.ok(orderService.mockNotify(orderNo));
    }
}
