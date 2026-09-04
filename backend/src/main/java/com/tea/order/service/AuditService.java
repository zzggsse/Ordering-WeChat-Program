package com.tea.order.service;

import com.tea.order.entity.AuditLog;
import com.tea.order.mapper.AuditLogMapper;
import com.tea.order.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogMapper auditLogMapper;

    public void log(LoginUser user, String action, String detail) {
        AuditLog log = new AuditLog()
                .setUserId(user == null ? null : user.getId())
                .setUsername(user == null ? null : (user.getType() + ":" + user.getId()))
                .setAction(action)
                .setDetail(detail)
                .setCreatedAt(LocalDateTime.now());
        auditLogMapper.insert(log);
    }
}
