package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.Paged;
import com.tea.order.entity.WechatUser;
import com.tea.order.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ApiResponse<Paged<WechatUser>> list(@RequestParam(required = false) String keyword,
                                               @RequestParam(defaultValue = "1") long page,
                                               @RequestParam(defaultValue = "20") long size) {
        return ApiResponse.ok(memberService.list(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<WechatUser> detail(@PathVariable Long id) {
        return ApiResponse.ok(memberService.detail(id));
    }

    @PatchMapping("/{id}/level")
    public ApiResponse<WechatUser> updateLevel(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ApiResponse.ok(memberService.updateLevel(id, body.get("level")));
    }
}
