
package com.example.demo.dao;

import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.WechatOfficialAccountMenuDO;

import java.util.List;

/**
 * 微信公众号菜单信息CURD
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
public interface WechatOfficialAccountMenuMapper extends BaseMapper<WechatOfficialAccountMenuDO> {
    int insertOne(WechatOfficialAccountMenuDO wechatOfficialAccountMenu);

    int insertBatch(List<WechatOfficialAccountMenuDO> list);

    int update(WechatOfficialAccountMenuDO wechatOfficialAccountMenu);

    int updateBatch(List<WechatOfficialAccountMenuDO> list);

    WechatOfficialAccountMenuDO queryById(Long id);

    List<WechatOfficialAccountMenuDO> query(WechatOfficialAccountMenuDO wechatOfficialAccountMenuDO);

    int queryCount(WechatOfficialAccountMenuDO wechatOfficialAccountMenuDO);
}
