package com.tea.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("wechat_user")
public class WechatUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private String level;
    private String role;
    private BigDecimal balance;
    private Integer totalOrders;
    private BigDecimal totalAmount;
    private BigDecimal maxRechargeAmount;
    private Long points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
