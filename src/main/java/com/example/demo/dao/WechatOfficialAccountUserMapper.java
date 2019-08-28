
package com.example.demo.dao;

import com.example.demo.configs.mapper.base.BaseMapper;
import com.example.demo.model.WechatOfficialAccountUserDO;

/**
 * 微信关注用户CRUD操作
 *
 * @author zhou.xy
 * @since 2019/5/6
 */
public interface WechatOfficialAccountUserMapper extends BaseMapper<WechatOfficialAccountUserDO> {
    int insertOne(WechatOfficialAccountUserDO wechatOfficialAccount);

    int update(WechatOfficialAccountUserDO wechatOfficialAccount);

    /**
     * 查询微信关注用户是否存在
     *
     * @param openId 用户的标识，对当前公众号唯一
     * @return
     */
    int selectCountByPrimaryKey(String openId);
}
