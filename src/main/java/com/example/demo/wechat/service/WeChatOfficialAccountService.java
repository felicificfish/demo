package com.example.demo.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.dao.WechatOfficialAccountMenuMapper;
import com.example.demo.model.WechatOfficialAccountMenuDO;
import com.example.demo.wechat.utils.MenuUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 微信公众号相关服务
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
@Log4j2
@Service
public class WeChatOfficialAccountService {
    @Autowired
    private WechatOfficialAccountMenuMapper wechatOfficialAccountMenuMapper;
    @Autowired
    private WeChatOfficialAccountApiService weChatOfficialAccountApiService;

    /**
     * 查询菜单
     *
     * @param appId 公众号开发者ID
     * @return java.util.List<com.example.demo.model.WechatMenuDO>
     * @author zhou.xy
     * @date 2019/8/28
     * @since 1.0
     */
    public List<WechatOfficialAccountMenuDO> queryMenuList(String appId) {
        Example example = new Example(WechatOfficialAccountMenuDO.class);
        example.createCriteria().andEqualTo("isDel", 0)
                .andEqualTo("appId", appId);
        example.setOrderByClause("menu_level ASC, sort ASC");
        return wechatOfficialAccountMenuMapper.selectByExample(example);
    }

    /**
     * 发布菜单
     *
     * @param appId    公众号开发者ID
     * @param userId
     * @param userName
     * @return void
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    public void publishMenu(String appId, Long userId, String userName) {
        if (StringUtils.isEmpty(appId)) {
            throw new ValidateException("公众号开发者ID不能为空");
        }
        Example example = new Example(WechatOfficialAccountMenuDO.class);
        example.createCriteria().andEqualTo("isDel", 0)
                .andEqualTo("appId", appId)
                .andEqualTo("isEnable", 1);
        example.setOrderByClause("menu_level ASC, sort ASC");
        List<WechatOfficialAccountMenuDO> menuDOList = wechatOfficialAccountMenuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(menuDOList)) {
            throw new ValidateException("没有可发布的菜单");
        }
        // TODO 操作日志
        JSONObject jsonObject = weChatOfficialAccountApiService.menuCreate(MenuUtil.prepareMenus(menuDOList));
        if (!jsonObject.getInteger("errcode").equals(0)) {
            throw new ValidateException(jsonObject.getString("errmsg"));
        }
    }
}
