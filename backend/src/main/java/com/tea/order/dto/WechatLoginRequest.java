package com.tea.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WechatLoginRequest {
    @NotBlank(message = "code 不能为空")
    private String code;
    private String nickname;
    private String avatar;
    private String phone;
}

