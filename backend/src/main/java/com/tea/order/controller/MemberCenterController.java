package com.tea.order.controller;

import com.tea.order.common.ApiResponse;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.RechargeRequest;
import com.tea.order.entity.Coupon;
import com.tea.order.entity.UserCoupon;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.WechatUserMapper;
import com.tea.order.security.CurrentUser;
import com.tea.order.service.PointsService;
import com.tea.order.vo.RichUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 顾客端：积分 / 充值 / 优惠券兑换 */
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberCenterController {

    private final WechatUserMapper wechatUserMapper;
    private final PointsService pointsService;

    private WechatUser me() {
        Long id = CurrentUser.get() == null ? null : CurrentUser.get().getId();
        if (id == null) throw new BusinessException("请先登录");
        WechatUser w = wechatUserMapper.selectById(id);
        if (w == null) throw new BusinessException("用户不存在");
        return w;
    }

    /** 充值：到账储值余额 + 赠送积分（单次 >=500 升钻石会员） */
    @PostMapping("/recharge")
    public ApiResponse<WechatUser> recharge(@RequestBody RechargeRequest req) {
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额需大于 0");
        }
        WechatUser w = me();
        pointsService.recharge(w, req.getAmount());
        w.setOpenid(null);
        return ApiResponse.ok(w);
    }

    /** 可兑换的优惠券列表 */
    @GetMapping("/coupons")
    public ApiResponse<List<Coupon>> coupons() {
        return ApiResponse.ok(pointsService.redeemableCoupons(me().getId()));
    }

    /** 用消费积分兑换优惠券 */
    @PostMapping("/redeem")
    public ApiResponse<Map<String, Object>> redeem(@RequestBody Map<String, Object> body) {
        Object cid = body.get("couponId");
        if (cid == null) throw new BusinessException("请选择要兑换的优惠券");
        WechatUser w = me();
        UserCoupon uc = pointsService.redeem(w, Long.valueOf(String.valueOf(cid)));
        return ApiResponse.ok(Map.of(
                "userCouponId", uc.getId(),
                "points", w.getPoints(),
                "balance", w.getBalance()));
    }

    /** 我的优惠券 */
    @GetMapping("/my-coupons")
    public ApiResponse<List<Map<String, Object>>> myCoupons() {
        return ApiResponse.ok(pointsService.myCoupons(me().getId()));
    }
}
