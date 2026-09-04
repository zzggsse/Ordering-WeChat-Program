package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.Paged;
import com.tea.order.entity.Product;
import com.tea.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Paged<Product>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "200") long size) {
        // 后台全量查询：不传 status 时返回所有商品（含下架、售罄），供工作台管理
        return ApiResponse.ok(productService.list(keyword, category, status, page, size));
    }
}