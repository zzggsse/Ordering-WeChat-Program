package com.tea.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tea.order.common.ApiResponse;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.RoleRequest;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.WechatUserMapper;
import com.tea.order.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manage")
@PreAuthorize("hasRole('OWNER')")
@RequiredArgsConstructor
public class ManageUserController {

    private final WechatUserMapper wechatUserMapper;

    /** 店长：查看用户（支持按手机号/昵称搜索） */
    @GetMapping("/users")
    public ApiResponse<List<WechatUser>> users(@RequestParam(required = false) String kw) {
        LambdaQueryWrapper<WechatUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(kw)) {
            wrapper.and(w -> w.like(WechatUser::getPhone, kw.trim())
                    .or().like(WechatUser::getNickname, kw.trim()));
        }
        // 店员排在最前，且最近设置/更新的在最上面
        wrapper.last(" ORDER BY (role = '店员') DESC, COALESCE(updated_at, created_at) DESC");
        List<WechatUser> list = wechatUserMapper.selectList(wrapper);
        list.forEach(u -> u.setOpenid(null));
        return ApiResponse.ok(list);
    }

    /** 店长：授予/收回店员权限（role 仅允许 店员 / 客户） */
    @PutMapping("/users/{id}/role")
    public ApiResponse<WechatUser> setRole(@PathVariable Long id, @RequestBody RoleRequest req) {
        String role = req.getRole() == null ? null : req.getRole().trim();
        if (!"店员".equals(role) && !"客户".equals(role)) {
            throw new BusinessException("只能分配「店员」或收回为「客户」");
        }
        Long me = CurrentUser.get() == null ? null : CurrentUser.get().getId();
        if (id.equals(me)) {
            throw new BusinessException("不能修改自己的权限，请联系其他店长");
        }
        WechatUser u = wechatUserMapper.selectById(id);
        if (u == null) throw new BusinessException("用户不存在");
        if ("店长".equals(u.getRole())) {
            throw new BusinessException("不能修改店长账号的权限");
        }
        u.setRole(role);
        u.setUpdatedAt(LocalDateTime.now());
        wechatUserMapper.updateById(u);
        return ApiResponse.ok(u);
    }
}
