package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.Paged;
import com.tea.order.dto.OrderCreateRequest;
import com.tea.order.security.CurrentUser;
import com.tea.order.security.LoginUser;
import com.tea.order.service.OrderService;
import com.tea.order.vo.OrderVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderVO> create(@Valid @RequestBody OrderCreateRequest req) {
        return ApiResponse.ok(orderService.create(req, CurrentUser.get()));
    }

    @GetMapping("/mine")
    public ApiResponse<Paged<OrderVO>> mine(@RequestParam(required = false) String scope,
                                            @RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "20") long size) {
        return ApiResponse.ok(orderService.mine(CurrentUser.get().getId(), scope, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderVO> detail(@PathVariable Long id) {
        return ApiResponse.ok(orderService.detail(id));
    }

    @PostMapping("/{id}/pay")
    public ApiResponse<OrderVO> pay(@PathVariable Long id) {
        return ApiResponse.ok(orderService.pay(id, CurrentUser.get()));
    }

    /** 顾客确认取餐 */
    @PostMapping("/{id}/receive")
    public ApiResponse<OrderVO> receive(@PathVariable Long id) {
        return ApiResponse.ok(orderService.receive(id, CurrentUser.get()));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderVO> cancel(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String reason = body == null ? null : body.get("reason");
        return ApiResponse.ok(orderService.cancel(id, CurrentUser.get(), reason));
    }
}

