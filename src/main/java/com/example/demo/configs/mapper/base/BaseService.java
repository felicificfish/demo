package com.example.demo.configs.mapper.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService<T> {
    @Autowired
    protected BaseMapper<T> mapper;

    public BaseService() {
    }

    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int deleteByCondition(T record) {
        return this.mapper.delete(record);
    }

    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public T queryOneByCondition(T record) {
        return this.mapper.selectOne(record);
    }

    public List<T> queryByCondition(T record) {
        return this.mapper.select(record);
    }

    public Page<T> query(T record, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> {
            this.mapper.select(record);
        });
    }

    public List<T> queryAll() {
        return this.mapper.selectAll();
    }

    public int countByCondition(T record) {
        return this.mapper.selectCount(record);
    }

    public boolean existByCondition(Long id) {
        return this.mapper.existsWithPrimaryKey(id);
    }
}
