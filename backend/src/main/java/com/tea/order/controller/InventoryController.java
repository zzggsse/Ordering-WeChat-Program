package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.dto.InventoryRequest;
import com.tea.order.entity.InventoryItem;
import com.tea.order.service.InventoryService;
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
@RequestMapping("/api/v1/inventory")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ApiResponse<List<InventoryItem>> list() {
        return ApiResponse.ok(inventoryService.list());
    }

    @PostMapping
    public ApiResponse<InventoryItem> create(@Valid @RequestBody InventoryRequest req) {
        return ApiResponse.ok(inventoryService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<InventoryItem> update(@PathVariable Long id, @Valid @RequestBody InventoryRequest req) {
        return ApiResponse.ok(inventoryService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ApiResponse.ok();
    }
}
