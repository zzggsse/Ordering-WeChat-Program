package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.service.DashboardService;
import com.tea.order.vo.StatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<StatsVO> stats() {
        return ApiResponse.ok(dashboardService.stats());
    }
}
