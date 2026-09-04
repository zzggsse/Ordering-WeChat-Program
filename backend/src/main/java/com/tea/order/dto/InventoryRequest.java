package com.tea.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotBlank(message = "原料名不能为空")
    private String name;
    private String spec;
    @NotNull(message = "库存不能为空")
    private Integer stock;
    @NotNull(message = "预警阈值不能为空")
    private Integer threshold;
    private String unit;
}
