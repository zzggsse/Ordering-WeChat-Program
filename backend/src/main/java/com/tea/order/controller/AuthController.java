package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.dto.LoginRequest;
import com.tea.order.dto.PhoneLoginRequest;
import com.tea.order.dto.ProfileRequest;
import com.tea.order.dto.SmsSendRequest;
import com.tea.order.dto.WechatLoginRequest;
import com.tea.order.security.CurrentUser;
import com.tea.order.security.LoginUser;
import com.tea.order.service.AuthService;
import com.tea.order.service.SmsService;
import com.tea.order.vo.LoginResponse;
import com.tea.order.vo.RichUserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;

    @Value("${app.sms.mock:true}")
    private boolean smsMock;

    /** PC 管理后台登录 */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.ok(authService.adminLogin(req));
    }

    /** 小程序微信授权登录 */
    @PostMapping("/wechat/login")
    public ApiResponse<LoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest req) {
        return ApiResponse.ok(authService.wechatLogin(req));
    }

    /** 发送短信验证码（mock 模式下返回验证码便于演示） */
    @PostMapping("/sms/send")
    public ApiResponse<Map<String, Object>> sendSms(@Valid @RequestBody SmsSendRequest req) {
        String code = smsService.sendCode(req.getPhone());
        return ApiResponse.ok(Map.of(
                "phone", req.getPhone(),
                "mock", smsMock,
                "code", smsMock ? code : null));
    }

    /** 手机号 + 验证码直接登录 */
    @PostMapping("/phone/login")
    public ApiResponse<LoginResponse> phoneLogin(@Valid @RequestBody PhoneLoginRequest req) {
        return ApiResponse.ok(authService.phoneLogin(req));
    }

    /** 修改昵称 / 头像 */
    @PutMapping("/profile")
    public ApiResponse<RichUserVO> updateProfile(@Valid @RequestBody ProfileRequest req) {
        return ApiResponse.ok(authService.updateProfile(CurrentUser.get(), req));
    }

    @GetMapping("/me")
    public ApiResponse<RichUserVO> me() {
        LoginUser user = CurrentUser.get();
        return ApiResponse.ok(authService.me(user));
    }
}

