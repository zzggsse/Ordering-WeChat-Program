package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.CouponRequest;
import com.tea.order.entity.Coupon;
import com.tea.order.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper couponMapper;

    public List<Coupon> list() {
        return couponMapper.selectList(new LambdaQueryWrapper<Coupon>()
                .orderByDesc(Coupon::getCreatedAt));
    }

    public Coupon create(CouponRequest req) {
        Coupon c = new Coupon()
                .setName(req.getName()).setType(req.getType() == null ? "FULL_REDUCTION" : req.getType())
                .setThreshold(req.getThreshold()).setDiscount(req.getDiscount())
                .setPointsCost(req.getPointsCost() == null ? 0 : req.getPointsCost())
                .setOncePerUser(req.getOncePerUser() == null ? 0 : req.getOncePerUser())
                .setTotal(req.getTotal() == null ? 0L : req.getTotal()).setUsed(0L)
                .setStartAt(req.getStartAt()).setEndAt(req.getEndAt())
                .setStatus(req.getStatus() == null ? 1 : req.getStatus())
                .setCreatedAt(LocalDateTime.now());
        couponMapper.insert(c);
        return c;
    }

    public Coupon update(Long id, CouponRequest req) {
        Coupon c = couponMapper.selectById(id);
        if (c == null) throw new BusinessException("优惠券不存在");
        if (req.getName() != null) c.setName(req.getName());
        if (req.getType() != null) c.setType(req.getType());
        if (req.getThreshold() != null) c.setThreshold(req.getThreshold());
        if (req.getDiscount() != null) c.setDiscount(req.getDiscount());
        if (req.getPointsCost() != null) c.setPointsCost(req.getPointsCost());
        if (req.getOncePerUser() != null) c.setOncePerUser(req.getOncePerUser());
        if (req.getTotal() != null) c.setTotal(req.getTotal());
        if (req.getStartAt() != null) c.setStartAt(req.getStartAt());
        if (req.getEndAt() != null) c.setEndAt(req.getEndAt());
        if (req.getStatus() != null) c.setStatus(req.getStatus());
        couponMapper.updateById(c);
        return c;
    }

    public void delete(Long id) {
        couponMapper.deleteById(id);
    }
}
