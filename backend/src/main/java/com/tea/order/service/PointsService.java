package com.tea.order.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tea.order.common.BusinessException;
import com.tea.order.entity.Coupon;
import com.tea.order.entity.UserCoupon;
import com.tea.order.entity.WechatUser;
import com.tea.order.mapper.CouponMapper;
import com.tea.order.mapper.UserCouponMapper;
import com.tea.order.mapper.WechatUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * 消费积分 / 会员等级 / 充值 / 优惠券积分兑换
 * 规则：普通会员消费 1:1 送积分；累计消费满 gold-threshold 升黄金会员 1:1.2；
 *      单次充值满 diamond-threshold 升钻石会员 1:2。
 */
@Service
@RequiredArgsConstructor
public class PointsService {

    private final WechatUserMapper wechatUserMapper;
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    @Value("${app.member.gold-threshold:500.0}")
    private BigDecimal goldThreshold;

    @Value("${app.member.diamond-threshold:500.0}")
    private BigDecimal diamondThreshold;

    /** 每充值 1 元赠送的积分（可配，默认 1 元=1 积分） */
    @Value("${app.member.recharge-points-factor:1.0}")
    private BigDecimal rechargePointsFactor;

    private static final String NORMAL = "普通会员";
    private static final String GOLD = "黄金会员";
    private static final String DIAMOND = "钻石会员";

    /** 根据充值/消费情况判定会员等级 */
    public String resolveLevel(WechatUser w) {
        BigDecimal maxRe = w.getMaxRechargeAmount();
        if (maxRe != null && maxRe.compareTo(diamondThreshold) >= 0) {
            return DIAMOND;
        }
        BigDecimal total = w.getTotalAmount();
        if (total != null && total.compareTo(goldThreshold) >= 0) {
            return GOLD;
        }
        return NORMAL;
    }

    public BigDecimal ratioOf(String level) {
        if (DIAMOND.equals(level)) return new BigDecimal("2.0");
        if (GOLD.equals(level)) return new BigDecimal("1.2");
        return new BigDecimal("1.0");
    }

    /** 订单完成时赠送消费积分（需在累计消费金额已更新后调用） */
    public void accrueConsumption(WechatUser w, BigDecimal amount) {
        String level = resolveLevel(w);
        w.setLevel(level);
        long pts = amount.multiply(ratioOf(level)).setScale(0, java.math.RoundingMode.DOWN).longValue();
        w.setPoints((w.getPoints() == null ? 0L : w.getPoints()) + pts);
    }

    /** 充值：到账储值余额 + 赠送积分，并记录单次最高充值以判定钻石资格 */
    public void recharge(WechatUser w, BigDecimal amount) {
        BigDecimal cur = w.getBalance() == null ? BigDecimal.ZERO : w.getBalance();
        w.setBalance(cur.add(amount));
        BigDecimal maxRe = w.getMaxRechargeAmount() == null ? BigDecimal.ZERO : w.getMaxRechargeAmount();
        if (amount.compareTo(maxRe) > 0) {
            w.setMaxRechargeAmount(amount);
        }
        long bonus = amount.multiply(rechargePointsFactor).setScale(0, java.math.RoundingMode.DOWN).longValue();
        w.setPoints((w.getPoints() == null ? 0L : w.getPoints()) + bonus);
        w.setLevel(resolveLevel(w));
        w.setUpdatedAt(LocalDateTime.now());
        wechatUserMapper.updateById(w);
    }

    /** 可兑换的优惠券（在用、且在活动期；每账号限兑一次的券已领过则隐藏） */
    public List<Coupon> redeemableCoupons(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Coupon> list = couponMapper.selectList(Wrappers.<Coupon>lambdaQuery()
                .eq(Coupon::getStatus, 1)
                .and(w -> w.isNull(Coupon::getStartAt).or().le(Coupon::getStartAt, now))
                .and(w -> w.isNull(Coupon::getEndAt).or().ge(Coupon::getEndAt, now))
                .orderByAsc(Coupon::getPointsCost)
                .orderByDesc(Coupon::getCreatedAt));
        Set<Long> redeemed = redeemedCouponIds(userId);
        List<Coupon> out = new ArrayList<>();
        for (Coupon c : list) {
            boolean onceClaimed = c.getOncePerUser() != null && c.getOncePerUser() == 1
                    && redeemed.contains(c.getId());
            if (!onceClaimed) out.add(c);
        }
        return out;
    }

    private Set<Long> redeemedCouponIds(Long userId) {
        if (userId == null) return new HashSet<>();
        List<UserCoupon> ucs = userCouponMapper.selectList(Wrappers.<UserCoupon>lambdaQuery()
                .eq(UserCoupon::getUserId, userId));
        Set<Long> ids = new HashSet<>();
        for (UserCoupon uc : ucs) ids.add(uc.getCouponId());
        return ids;
    }

    /** 用消费积分兑换优惠券 */
    @Transactional
    public UserCoupon redeem(WechatUser w, Long couponId) {
        Coupon c = couponMapper.selectById(couponId);
        if (c == null || c.getStatus() == null || c.getStatus() != 1) {
            throw new BusinessException("优惠券不存在或已下架");
        }
        LocalDateTime now = LocalDateTime.now();
        if (c.getStartAt() != null && c.getStartAt().isAfter(now)) throw new BusinessException("优惠券活动未开始");
        if (c.getEndAt() != null && c.getEndAt().isBefore(now)) throw new BusinessException("优惠券已过期");
        if (c.getUsed() != null && c.getTotal() != null && c.getUsed() >= c.getTotal()) {
            throw new BusinessException("优惠券已兑完");
        }
        int cost = c.getPointsCost() == null ? 0 : c.getPointsCost();
        // 限领券：每个账号同一张限兑一次
        if (c.getOncePerUser() != null && c.getOncePerUser() == 1) {
            Long exists = userCouponMapper.selectCount(Wrappers.<UserCoupon>lambdaQuery()
                    .eq(UserCoupon::getUserId, w.getId())
                    .eq(UserCoupon::getCouponId, couponId));
            if (exists != null && exists > 0) {
                throw new BusinessException("该优惠券每账号限兑一次");
            }
        }
        long points = w.getPoints() == null ? 0L : w.getPoints();
        if (points < cost) {
            throw new BusinessException("消费积分不足");
        }
        w.setPoints(points - cost);
        w.setUpdatedAt(now);
        wechatUserMapper.updateById(w);

        c.setUsed((c.getUsed() == null ? 0L : c.getUsed()) + 1);
        couponMapper.updateById(c);

        UserCoupon uc = new UserCoupon()
                .setUserId(w.getId()).setCouponId(couponId)
                .setStatus("UNUSED").setCreatedAt(now);
        userCouponMapper.insert(uc);
        return uc;
    }

    /** 我的优惠券（带券名与满减信息） */
    public List<Map<String, Object>> myCoupons(Long userId) {
        List<UserCoupon> list = userCouponMapper.selectList(Wrappers.<UserCoupon>lambdaQuery()
                .eq(UserCoupon::getUserId, userId).orderByDesc(UserCoupon::getCreatedAt));
        List<Map<String, Object>> out = new ArrayList<>();
        for (UserCoupon uc : list) {
            Coupon c = couponMapper.selectById(uc.getCouponId());
            Map<String, Object> m = new HashMap<>();
            m.put("id", uc.getId());
            m.put("couponId", uc.getCouponId());
            m.put("name", c != null ? c.getName() : "优惠券");
            m.put("threshold", c != null ? c.getThreshold() : null);
            m.put("discount", c != null ? c.getDiscount() : null);
            m.put("status", uc.getStatus());
            m.put("createdAt", uc.getCreatedAt());
            out.add(m);
        }
        return out;
    }
}
