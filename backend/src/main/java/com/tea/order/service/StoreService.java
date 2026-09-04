package com.tea.order.service;

import com.tea.order.common.BusinessException;
import com.tea.order.dto.StoreRequest;
import com.tea.order.entity.Store;
import com.tea.order.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;
    private static final long DEFAULT_STORE_ID = 1L;

    public Store current() {
        Store store = storeMapper.selectById(DEFAULT_STORE_ID);
        if (store == null) {
            throw new BusinessException("门店不存在");
        }
        return store;
    }

    public Store update(StoreRequest req) {
        Store store = current();
        if (req.getName() != null) store.setName(req.getName());
        if (req.getAddress() != null) store.setAddress(req.getAddress());
        if (req.getPhone() != null) store.setPhone(req.getPhone());
        if (req.getBusinessHours() != null) store.setBusinessHours(req.getBusinessHours());
        if (req.getStatus() != null) store.setStatus(req.getStatus());
        store.setUpdatedAt(LocalDateTime.now());
        storeMapper.updateById(store);
        return store;
    }
}
