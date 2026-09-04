package com.tea.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("store")
public class Store {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String businessHours;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
