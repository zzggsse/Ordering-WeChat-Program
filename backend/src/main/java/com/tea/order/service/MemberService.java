package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.order.common.BusinessException;
import com.tea.order.common.Paged;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.WechatUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final WechatUserMapper wechatUserMapper;

    public Paged<WechatUser> list(String keyword, long page, long size) {
        LambdaQueryWrapper<WechatUser> qw = new LambdaQueryWrapper<WechatUser>()
                .and(StringUtils.hasText(keyword), w -> w
                        .like(WechatUser::getNickname, keyword).or()
                        .like(WechatUser::getPhone, keyword))
                .orderByDesc(WechatUser::getTotalAmount);
        Page<WechatUser> p = wechatUserMapper.selectPage(new Page<>(page, size), qw);
        return Paged.of(p);
    }

    public WechatUser detail(Long id) {
        WechatUser w = wechatUserMapper.selectById(id);
        if (w == null) throw new BusinessException("会员不存在");
        return w;
    }

    public WechatUser updateLevel(Long id, String level) {
        WechatUser w = detail(id);
        w.setLevel(level);
        w.setUpdatedAt(LocalDateTime.now());
        wechatUserMapper.updateById(w);
        return w;
    }
}

