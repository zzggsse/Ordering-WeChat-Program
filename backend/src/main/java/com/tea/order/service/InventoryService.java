package com.tea.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.order.common.BusinessException;
import com.tea.order.dto.InventoryRequest;
import com.tea.order.entity.InventoryItem;
import com.tea.order.mapper.InventoryItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemMapper inventoryItemMapper;

    public List<InventoryItem> list() {
        return inventoryItemMapper.selectList(new LambdaQueryWrapper<InventoryItem>()
                .orderByAsc(InventoryItem::getId));
    }

    public InventoryItem create(InventoryRequest req) {
        InventoryItem item = new InventoryItem()
                .setStoreId(1L).setName(req.getName()).setSpec(req.getSpec())
                .setStock(req.getStock()).setThreshold(req.getThreshold())
                .setUnit(req.getUnit()).setStatus(1).setUpdatedAt(LocalDateTime.now());
        inventoryItemMapper.insert(item);
        return item;
    }

    public InventoryItem update(Long id, InventoryRequest req) {
        InventoryItem item = inventoryItemMapper.selectById(id);
        if (item == null) throw new BusinessException("库存项不存在");
        if (req.getName() != null) item.setName(req.getName());
        if (req.getSpec() != null) item.setSpec(req.getSpec());
        if (req.getStock() != null) item.setStock(req.getStock());
        if (req.getThreshold() != null) item.setThreshold(req.getThreshold());
        if (req.getUnit() != null) item.setUnit(req.getUnit());
        item.setUpdatedAt(LocalDateTime.now());
        inventoryItemMapper.updateById(item);
        return item;
    }

    public void delete(Long id) {
        inventoryItemMapper.deleteById(id);
    }
}
