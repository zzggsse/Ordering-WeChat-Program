package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.dto.CouponRequest;
import com.tea.order.entity.Coupon;
import com.tea.order.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ApiResponse<List<Coupon>> list() {
        return ApiResponse.ok(couponService.list());
    }

    @PostMapping
    public ApiResponse<Coupon> create(@Valid @RequestBody CouponRequest req) {
        return ApiResponse.ok(couponService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Coupon> update(@PathVariable Long id, @Valid @RequestBody CouponRequest req) {
        return ApiResponse.ok(couponService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        couponService.delete(id);
        return ApiResponse.ok();
    }
}
