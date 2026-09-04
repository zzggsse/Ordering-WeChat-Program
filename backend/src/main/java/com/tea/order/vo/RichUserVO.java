package com.tea.order.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RichUserVO {
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
}
