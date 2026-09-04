package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ApiResponse<List<Map<String, Object>>> sales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ApiResponse.ok(reportService.salesByDay(start, end));
    }

    @GetMapping("/products")
    public ApiResponse<List<Map<String, Object>>> products(@RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.ok(reportService.productSales(limit));
    }

    @GetMapping("/timeslots")
    public ApiResponse<List<Map<String, Object>>> timeslots(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(reportService.timeSlots(date));
    }
}
