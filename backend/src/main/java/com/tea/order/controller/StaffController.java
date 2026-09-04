package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.dto.StaffRequest;
import com.tea.order.entity.AdminUser;
import com.tea.order.service.StaffService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@PreAuthorize("hasRole('OWNER')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ApiResponse<List<AdminUser>> list() {
        return ApiResponse.ok(staffService.list());
    }

    @PostMapping
    public ApiResponse<AdminUser> create(@Valid @RequestBody StaffRequest req) {
        return ApiResponse.ok(staffService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminUser> update(@PathVariable Long id, @Valid @RequestBody StaffRequest req) {
        return ApiResponse.ok(staffService.update(id, req));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<AdminUser> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        staffService.toggleStatus(id, status);
        return ApiResponse.ok(staffService.list().stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ApiResponse.ok();
    }
}
