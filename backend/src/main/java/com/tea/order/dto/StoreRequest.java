package com.tea.order.dto;

import lombok.Data;

@Data
public class StoreRequest {
    private String name;
    private String address;
    private String phone;
    private String businessHours;
    private Integer status = 1;
}
