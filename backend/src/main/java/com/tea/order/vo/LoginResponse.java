package com.tea.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String type;      // ADMIN / CUSTOMER
    private String role;
    private String displayName;
    private Long storeId;
}
