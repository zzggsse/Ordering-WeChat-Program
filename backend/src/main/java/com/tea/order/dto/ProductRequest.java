package com.tea.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "商品名不能为空")
    private String name;
    private Long categoryId;
    private String category;
    private String image;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    private String description;
    private Integer status = 1;
    private Integer recommended = 0;
    private Integer soldout = 0;
}
