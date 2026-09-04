package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.StaffRequest;
import com.tea.order.entity.AdminUser;
import com.tea.order.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final AdminUserMapper adminUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<AdminUser> list() {
        return adminUserMapper.selectList(new LambdaQueryWrapper<AdminUser>()
                .orderByAsc(AdminUser::getId));
    }

    public AdminUser create(StaffRequest req) {
        boolean exists = adminUserMapper.selectCount(
                new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, req.getUsername())) > 0;
        if (exists) throw new BusinessException("用户名已存在");
        AdminUser u = new AdminUser()
                .setUsername(req.getUsername())
                .setPassword(passwordEncoder.encode(req.getPassword()))
                .setRealName(req.getRealName())
                .setRole(req.getRole() == null ? "店员" : req.getRole())
                .setStoreId(req.getStoreId() == null ? 1L : req.getStoreId())
                .setStatus(1)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        adminUserMapper.insert(u);
        return u;
    }

    public AdminUser update(Long id, StaffRequest req) {
        AdminUser u = adminUserMapper.selectById(id);
        if (u == null) throw new BusinessException("员工不存在");
        if (StringUtils.hasText(req.getPassword())) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if (req.getRealName() != null) u.setRealName(req.getRealName());
        if (req.getRole() != null) u.setRole(req.getRole());
        if (req.getStoreId() != null) u.setStoreId(req.getStoreId());
        u.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(u);
        return u;
    }

    public void toggleStatus(Long id, Integer status) {
        AdminUser u = adminUserMapper.selectById(id);
        if (u == null) throw new BusinessException("员工不存在");
        u.setStatus(status);
        u.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(u);
    }

    public void delete(Long id) {
        adminUserMapper.deleteById(id);
    }
}
