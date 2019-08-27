
package com.example.demo.dao;


import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.WechatMenuDO;

import java.util.List;

/**
 * 微信公众号菜单信息CURD
 *
 * @author zhou.xy
 * @since 2019/6/10
 */
public interface WechatMenuMapper extends BaseMapper<WechatMenuDO> {
    int insertOne(WechatMenuDO wechatMenu);

    int insertBatch(List<WechatMenuDO> list);

    int update(WechatMenuDO wechatMenu);

    int updateBatch(List<WechatMenuDO> list);

    WechatMenuDO queryById(Long id);

    List<WechatMenuDO> query(WechatMenuDO wechatMenuDO);

    int queryCount(WechatMenuDO wechatMenuDO);
}
