package com.tea.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private String name;
    private Integer sort;
    private Integer status;
}
