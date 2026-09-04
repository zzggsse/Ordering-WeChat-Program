package com.tea.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("inventory_item")
public class InventoryItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private String name;
    private String spec;
    private Integer stock;
    private Integer threshold;
    private String unit;
    private Integer status;
    private LocalDateTime updatedAt;
}
