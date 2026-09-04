package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.order.common.BusinessException;
import com.tea.order.common.Paged;
import com.tea.order.dto.ProductRequest;
import com.tea.order.entity.Product;
import com.tea.order.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public Paged<Product> list(String keyword, String category, Integer status, long page, long size) {
        LambdaQueryWrapper<Product> qw = new LambdaQueryWrapper<Product>()
                .like(StringUtils.hasText(keyword), Product::getName, keyword)
                .eq(StringUtils.hasText(category), Product::getCategory, category)
                .eq(status != null, Product::getStatus, status)
                .orderByDesc(Product::getRecommended)
                .orderByDesc(Product::getSales);
        Page<Product> p = productMapper.selectPage(new Page<>(page, size), qw);
        return Paged.of(p);
    }

    public Product byId(Long id) {
        Product p = productMapper.selectById(id);
        if (p == null) throw new BusinessException("商品不存在");
        return p;
    }

    public Product create(ProductRequest req) {
        Product p = new Product()
                .setStoreId(1L)
                .setCategoryId(req.getCategoryId())
                .setCategory(req.getCategory())
                .setName(req.getName())
                .setImage(req.getImage())
                .setPrice(req.getPrice())
                .setDescription(req.getDescription())
                .setSales(0L)
                .setStatus(req.getStatus() == null ? 1 : req.getStatus())
                .setRecommended(req.getRecommended() == null ? 0 : req.getRecommended())
                .setSoldout(req.getSoldout() == null ? 0 : req.getSoldout())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        productMapper.insert(p);
        return p;
    }

    public Product update(Long id, ProductRequest req) {
        Product p = byId(id);
        if (req.getName() != null) p.setName(req.getName());
        if (req.getCategoryId() != null) p.setCategoryId(req.getCategoryId());
        if (req.getCategory() != null) p.setCategory(req.getCategory());
        if (req.getImage() != null) p.setImage(req.getImage());
        if (req.getPrice() != null) p.setPrice(req.getPrice());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getStatus() != null) p.setStatus(req.getStatus());
        if (req.getRecommended() != null) p.setRecommended(req.getRecommended());
        if (req.getSoldout() != null) p.setSoldout(req.getSoldout());
        p.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(p);
        return p;
    }

    /** 统一状态更新：status 在售/下架，soldout 售罄开关。任一为 null 表示不修改。 */
    public Product updateStatus(Long id, Integer status, Integer soldout) {
        Product p = byId(id);
        if (status != null) p.setStatus(status);
        if (soldout != null) p.setSoldout(soldout);
        p.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(p);
        return p;
    }

    public Product updateStock(Long id, Integer stock) {
        if (stock == null || stock < 0) throw new com.tea.order.common.BusinessException("库存数量需大于等于 0");
        Product p = byId(id);
        p.setStock(stock);
        p.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(p);
        return p;
    }

    public void delete(Long id) {
        Product p = byId(id);
        productMapper.deleteById(p.getId());
    }
}
