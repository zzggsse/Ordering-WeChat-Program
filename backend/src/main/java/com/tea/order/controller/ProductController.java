package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.Paged;
import com.tea.order.dto.ProductRequest;
import com.tea.order.entity.Product;
import com.tea.order.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Paged<Product>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "20") long size) {
        // 顾客端默认只显示上架商品；传 status 时按需过滤
        Integer eff = status == null ? 1 : status;
        return ApiResponse.ok(productService.list(keyword, category, eff, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> detail(@PathVariable Long id) {
        return ApiResponse.ok(productService.byId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Product> create(@Valid @RequestBody ProductRequest req) {
        return ApiResponse.ok(productService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return ApiResponse.ok(productService.update(id, req));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Product> updateStatus(@PathVariable Long id,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) Integer soldout) {
        return ApiResponse.ok(productService.updateStatus(id, status, soldout));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok();
    }

    /** 设置单品库存数量 */
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<Product> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return ApiResponse.ok(productService.updateStock(id, stock));
    }
}
