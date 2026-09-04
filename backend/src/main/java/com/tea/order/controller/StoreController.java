package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.dto.StoreRequest;
import com.tea.order.entity.Store;
import com.tea.order.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/current")
    public ApiResponse<Store> current() {
        return ApiResponse.ok(storeService.current());
    }

    @PutMapping("/current")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Store> update(@RequestBody StoreRequest req) {
        return ApiResponse.ok(storeService.update(req));
    }
}
