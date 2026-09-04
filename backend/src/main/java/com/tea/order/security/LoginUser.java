package com.tea.order.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUser {
    private Long id;
    private String type;   // ADMIN / CUSTOMER
    private String role;   // 老板/店长/店员/财务/运营 / CUSTOMER
    private Long storeId;
}
