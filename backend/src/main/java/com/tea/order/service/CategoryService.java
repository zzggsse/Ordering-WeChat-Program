package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.CategoryRequest;
import com.tea.order.entity.Category;
import com.tea.order.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<Category> list() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort));
    }

    public Category create(CategoryRequest req) {
        Category c = new Category().setStoreId(1L).setName(req.getName())
                .setSort(req.getSort() == null ? 0 : req.getSort())
                .setStatus(req.getStatus() == null ? 1 : req.getStatus());
        categoryMapper.insert(c);
        return c;
    }

    public Category update(Long id, CategoryRequest req) {
        Category c = categoryMapper.selectById(id);
        if (c == null) throw new BusinessException("分类不存在");
        if (req.getName() != null) c.setName(req.getName());
        if (req.getSort() != null) c.setSort(req.getSort());
        if (req.getStatus() != null) c.setStatus(req.getStatus());
        categoryMapper.updateById(c);
        return c;
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
