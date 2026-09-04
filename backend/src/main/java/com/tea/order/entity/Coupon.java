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
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type;
    private BigDecimal threshold;
    private BigDecimal discount;
    private Integer pointsCost;
    private Integer oncePerUser;
    private Long total;
    private Long used;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private LocalDateTime createdAt;
}
