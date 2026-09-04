package com.tea.order.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderVO {
    private Long id;
    private String orderNo;
    private Long storeId;
    private Long userId;
    private String pickupType;
    private String status;
    private String statusLabel;
    private BigDecimal totalAmount;
    private String pickupNo;
    private String remark;
    private String cancelReason;
    private String rejectReason;
    private LocalDateTime paidAt;
    private LocalDateTime doneAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemVO> items;
}
