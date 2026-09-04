package com.tea.order.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.LoginRequest;
import com.tea.order.dto.PhoneLoginRequest;
import com.tea.order.dto.ProfileRequest;
import com.tea.order.dto.WechatLoginRequest;
import com.tea.order.entity.AdminUser;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.AdminUserMapper;
import com.tea.order.mapper.WechatUserMapper;
import com.tea.order.security.JwtUtil;
import com.tea.order.security.LoginUser;
import com.tea.order.vo.LoginResponse;
import com.tea.order.vo.RichUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserMapper adminUserMapper;
    private final WechatUserMapper wechatUserMapper;
    private final SmsService smsService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse adminLogin(LoginRequest req) {
        AdminUser admin = adminUserMapper.selectOne(
                Wrappers.<AdminUser>lambdaQuery().eq(AdminUser::getUsername, req.getUsername()));
        if (admin == null || admin.getStatus() == null || admin.getStatus() != 1
                || !passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtil.generate(admin.getId(), "ADMIN", admin.getRole(), admin.getStoreId());
        return new LoginResponse(token, admin.getId(), "ADMIN", admin.getRole(),
                StringUtils.hasText(admin.getRealName()) ? admin.getRealName() : admin.getUsername(),
                admin.getStoreId());
    }

    public LoginResponse wechatLogin(WechatLoginRequest req) {
        // 课程演示：无真实微信鉴权，直接用 code 作为 openid，换入可替换为 code2session
        String openid = StringUtils.hasText(req.getCode()) ? "wx_" + req.getCode() : "wx_guest_" + System.currentTimeMillis();
        WechatUser user = wechatUserMapper.selectOne(
                Wrappers.<WechatUser>lambdaQuery().eq(WechatUser::getOpenid, openid));
        if (user == null) {
            user = new WechatUser()
                    .setOpenid(openid)
                    .setNickname(StringUtils.hasText(req.getNickname()) ? req.getNickname() : "微信用户")
                    .setAvatar(req.getAvatar())
                    .setPhone(req.getPhone())
                    .setLevel("普通会员")
                    .setRole("客户")
                    .setBalance(BigDecimal.ZERO)
                    .setTotalOrders(0)
                    .setTotalAmount(BigDecimal.ZERO)
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedAt(LocalDateTime.now());
            wechatUserMapper.insert(user);
        } else {
            if (StringUtils.hasText(req.getNickname())) {
                user.setNickname(req.getNickname());
            }
            if (StringUtils.hasText(req.getAvatar())) {
                user.setAvatar(req.getAvatar());
            }
            if (StringUtils.hasText(req.getPhone()) && !StringUtils.hasText(user.getPhone())) {
                user.setPhone(req.getPhone());
            }
            if (!StringUtils.hasText(user.getRole())) {
                user.setRole("客户");
            }
            user.setUpdatedAt(LocalDateTime.now());
            wechatUserMapper.updateById(user);
        }
        String token = jwtUtil.generate(user.getId(), "CUSTOMER", user.getRole(), 1L);
        return new LoginResponse(token, user.getId(), "CUSTOMER", user.getRole(),
                user.getNickname(), 1L);
    }

    /** 手机号 + 验证码直接登录：无验证码则建号，有则绑定/校验。 */
    public LoginResponse phoneLogin(PhoneLoginRequest req) {
        if (!smsService.verify(req.getPhone(), req.getCode())) {
            throw new BusinessException("验证码错误或已过期");
        }
        smsService.clear(req.getPhone());
        WechatUser user = wechatUserMapper.selectOne(
                Wrappers.<WechatUser>lambdaQuery().eq(WechatUser::getPhone, req.getPhone()));
        if (user == null) {
            user = new WechatUser()
                    .setOpenid("phone_" + req.getPhone())
                    .setPhone(req.getPhone())
                    .setNickname(maskNickname(req.getPhone()))
                    .setLevel("普通会员")
                    .setRole("客户")
                    .setBalance(BigDecimal.ZERO)
                    .setTotalOrders(0)
                    .setTotalAmount(BigDecimal.ZERO)
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedAt(LocalDateTime.now());
            wechatUserMapper.insert(user);
        } else {
            if (!StringUtils.hasText(user.getRole())) {
                user.setRole("客户");
                user.setUpdatedAt(LocalDateTime.now());
                wechatUserMapper.updateById(user);
            }
        }
        String token = jwtUtil.generate(user.getId(), "CUSTOMER", user.getRole(), 1L);
        return new LoginResponse(token, user.getId(), "CUSTOMER", user.getRole(),
                StringUtils.hasText(user.getNickname()) ? user.getNickname() : maskNickname(req.getPhone()),
                1L);
    }

    /** 顾客修改昵称 / 头像 */
    public RichUserVO updateProfile(LoginUser user, ProfileRequest req) {
        if (!"CUSTOMER".equals(user.getType())) {
            throw new BusinessException("仅顾客账号支持修改资料");
        }
        WechatUser w = wechatUserMapper.selectById(user.getId());
        if (w == null) throw new BusinessException("用户不存在");
        if (StringUtils.hasText(req.getNickname())) {
            w.setNickname(req.getNickname());
        }
        if (StringUtils.hasText(req.getAvatar())) {
            w.setAvatar(req.getAvatar());
        }
        w.setUpdatedAt(LocalDateTime.now());
        wechatUserMapper.updateById(w);
        return toRich(w);
    }

    public RichUserVO me(LoginUser user) {
        if ("ADMIN".equals(user.getType())) {
            AdminUser admin = adminUserMapper.selectById(user.getId());
            RichUserVO vo = new RichUserVO();
            vo.setId(admin.getId());
            vo.setNickname(StringUtils.hasText(admin.getRealName()) ? admin.getRealName() : admin.getUsername());
            vo.setLevel(admin.getRole());
            vo.setRole(admin.getRole());
            return vo;
        }
        WechatUser w = wechatUserMapper.selectById(user.getId());
        return toRich(w);
    }

    public RichUserVO toRich(WechatUser w) {
        RichUserVO vo = new RichUserVO();
        vo.setId(w.getId());
        vo.setOpenid(w.getOpenid());
        vo.setNickname(w.getNickname());
        vo.setAvatar(w.getAvatar());
        vo.setPhone(w.getPhone());
        vo.setLevel(w.getLevel());
        vo.setRole(w.getRole());
        vo.setBalance(w.getBalance());
        vo.setTotalOrders(w.getTotalOrders());
        vo.setTotalAmount(w.getTotalAmount());
        vo.setMaxRechargeAmount(w.getMaxRechargeAmount());
        vo.setPoints(w.getPoints());
        return vo;
    }

    private String maskNickname(String phone) {
        return "用户" + phone.substring(phone.length() - 4);
    }
}
