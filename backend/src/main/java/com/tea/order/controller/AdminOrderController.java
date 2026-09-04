package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.Paged;
import com.tea.order.dto.RejectRequest;
import com.tea.order.dto.RefundRequest;
import com.tea.order.security.CurrentUser;
import com.tea.order.service.AuditService;
import com.tea.order.service.OrderService;
import com.tea.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/orders")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final AuditService auditService;

    @GetMapping
    public ApiResponse<Paged<OrderVO>> list(@RequestParam(required = false) String status,
                                            @RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "20") long size) {
        return ApiResponse.ok(orderService.adminList(status, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderVO> detail(@PathVariable Long id) {
        return ApiResponse.ok(orderService.detail(id));
    }

    @PostMapping("/{id}/accept")
    public ApiResponse<OrderVO> accept(@PathVariable Long id) {
        auditService.log(CurrentUser.get(), "接单", "订单#" + id + " 已接单");
        return ApiResponse.ok(orderService.accept(id, CurrentUser.get()));
    }

    @PostMapping("/{id}/ready")
    public ApiResponse<OrderVO> ready(@PathVariable Long id) {
        auditService.log(CurrentUser.get(), "出杯叫号", "订单#" + id + " 出杯");
        return ApiResponse.ok(orderService.ready(id, CurrentUser.get()));
    }

    @PostMapping("/{id}/done")
    public ApiResponse<OrderVO> done(@PathVariable Long id) {
        auditService.log(CurrentUser.get(), "取餐完成", "订单#" + id + " 完成");
        return ApiResponse.ok(orderService.done(id, CurrentUser.get()));
    }

    @PostMapping("/{id}/reject")
    public ApiResponse<OrderVO> reject(@PathVariable Long id, @RequestBody RejectRequest req) {
        return ApiResponse.ok(orderService.reject(id, req.getReason()));
    }

    @PostMapping("/{id}/refund/approve")
    public ApiResponse<OrderVO> refundApprove(@PathVariable Long id, @RequestBody(required = false) RejectRequest req) {
        String reason = req == null ? null : req.getReason();
        return ApiResponse.ok(orderService.refundApprove(id, reason));
    }
}
