package com.example.demo.configs.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Data
public class PageData implements Serializable {
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    private List<?> content;
}
