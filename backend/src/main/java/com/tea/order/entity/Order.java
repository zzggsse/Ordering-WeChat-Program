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
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long storeId;
    private Long userId;
    private String pickupType;
    private String status;
    private BigDecimal totalAmount;
    private String pickupNo;
    private String remark;
    private String cancelReason;
    private String rejectReason;
    private LocalDateTime paidAt;
    private LocalDateTime doneAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
