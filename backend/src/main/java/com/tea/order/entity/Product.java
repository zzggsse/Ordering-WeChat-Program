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
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private Long categoryId;
    private String category;
    private String name;
    private String image;
    private BigDecimal price;
    private String description;
    private Long sales;
    private Integer stock;
    private Integer status;
    private Integer recommended;
    private Integer soldout;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
