package com.tea.order.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paged<T> {
    private long total;
    private long page;
    private long size;
    private List<T> records;

    public static <T> Paged<T> of(IPage<T> p) {
        return new Paged<>(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords());
    }
}
