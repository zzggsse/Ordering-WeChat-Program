package com.tea.order.config;

import com.tea.order.entity.AdminUser;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.AdminUserMapper;
import com.tea.order.mapper.WechatUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminUserMapper adminUserMapper;
    private final WechatUserMapper wechatUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCustomer();
    }

    private void seedAdmin() {
        Long count = adminUserMapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        AdminUser admin = new AdminUser()
                .setUsername("admin")
                .setPassword(passwordEncoder.encode("123456"))
                .setRealName("老板")
                .setRole("老板")
                .setStoreId(1L)
                .setStatus(1)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        adminUserMapper.insert(admin);
        log.info("已初始化管理员账号 admin / 123456");
    }

    private void seedCustomer() {
        Long count = wechatUserMapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        WechatUser user = new WechatUser()
                .setOpenid("demo_customer_0001")
                .setNickname("演示顾客")
                .setLevel("普通会员")
                .setBalance(java.math.BigDecimal.ZERO)
                .setTotalOrders(0)
                .setTotalAmount(java.math.BigDecimal.ZERO)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        wechatUserMapper.insert(user);
        log.info("已初始化演示顾客账号 demo_customer_0001");
    }
}
