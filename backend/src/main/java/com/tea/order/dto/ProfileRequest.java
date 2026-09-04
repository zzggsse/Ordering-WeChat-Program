package com.tea.order.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequest {
    @Size(max = 20, message = "昵称最多 20 个字符")
    private String nickname;
    private String avatar;
}
