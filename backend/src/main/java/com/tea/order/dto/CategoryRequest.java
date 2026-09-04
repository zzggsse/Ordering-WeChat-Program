package com.tea.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "分类名不能为空")
    private String name;
    private Integer sort = 0;
    private Integer status = 1;
}
