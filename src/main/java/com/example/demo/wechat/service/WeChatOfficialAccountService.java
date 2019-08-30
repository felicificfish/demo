package com.example.demo.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.dao.WechatOfficialAccountMenuMapper;
import com.example.demo.dao.WechatOfficialAccountUserMapper;
import com.example.demo.model.WechatOfficialAccountMenuDO;
import com.example.demo.model.WechatOfficialAccountUserDO;
import com.example.demo.utils.RedisTemplateUtil;
import com.example.demo.wechat.constant.MenuTypeEnum;
import com.example.demo.wechat.model.WeChatTemplateData;
import com.example.demo.wechat.model.WeChatTemplateMsg;
import com.example.demo.wechat.utils.MenuUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 微信公众号相关服务
 *
 * @author zhou.xy
 * @since 2019/8/28
 */
@Log4j2
@Service
public class WeChatOfficialAccountService {
    public static final String WECHAT_USER_INFO_CACHE_PREFIX = "wechat_user_info_";
    @Autowired
    private WechatOfficialAccountMenuMapper wechatOfficialAccountMenuMapper;
    @Autowired
    private WeChatOfficialAccountApiService weChatOfficialAccountApiService;
    @Autowired
    private WechatOfficialAccountUserMapper wechatOfficialAccountUserMapper;

    /**
     * 查询菜单
     *
     * @param menuDO
     * @return java.util.List<com.example.demo.model.WechatMenuDO>
     * @author zhou.xy
     * @date 2019/8/28
     * @since 1.0
     */
    public List<WechatOfficialAccountMenuDO> queryMenuList(WechatOfficialAccountMenuDO menuDO) {
        return wechatOfficialAccountMenuMapper.query(menuDO);
    }

    /**
     * 查询菜单
     *
     * @param appId     公众号开发者ID
     * @param menuLevel 菜单等级：1-一级菜单；2-二级菜单；
     * @return java.util.List<com.example.demo.model.WechatOfficialAccountMenuDO>
     * @author zhou.xy
     * @date 2019/8/28
     * @since 1.0
     */
    public List<WechatOfficialAccountMenuDO> queryMenuList(String appId, Integer menuLevel) {
        Example example = new Example(WechatOfficialAccountMenuDO.class);
        example.createCriteria().andEqualTo("isDel", 0)
                .andEqualTo("appId", appId)
                .andEqualTo("menuLevel", menuLevel);
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
        if (!Objects.equals(jsonObject.getInteger("errcode"), 0)) {
            throw new ValidateException(jsonObject.getString("errmsg"));
        }
    }

    /**
     * 删除菜单
     *
     * @param menuIdList
     * @param userId
     * @param userName
     * @return void
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public void deleteMenu(List<Long> menuIdList, Long userId, String userName) {
        Example example = new Example(WechatOfficialAccountMenuDO.class);
        example.createCriteria().andIn("menuId", menuIdList)
                .andEqualTo("isEnable", 1)
                .andEqualTo("isDel", 0);
        int count = wechatOfficialAccountMenuMapper.selectCountByExample(example);
        if (count > 0) {
            throw new ValidateException("存在启用菜单，不能删除！");
        }

        WechatOfficialAccountMenuDO menuDO = new WechatOfficialAccountMenuDO();
        menuDO.setMenuIdList(menuIdList);
        menuDO.setModifiedon(new Date());
        menuDO.setModifier(userName);
        menuDO.setModifierId(userId);
        wechatOfficialAccountMenuMapper.deleteByMenuId(menuDO);
    }

    /**
     * 删除已发布菜单
     *
     * @param userId
     * @param userName
     * @return void
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public void revokeMenu(Long userId, String userName) {
        // TODO 操作日志
        JSONObject jsonObject = weChatOfficialAccountApiService.menuDelete();
        if (!Objects.equals(jsonObject.getInteger("errcode"), 0)) {
            throw new ValidateException(jsonObject.getString("errmsg"));
        }
    }

    /**
     * 新增菜单
     *
     * @param menuDO
     * @param userId
     * @param userName
     * @return void
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public void addMenu(WechatOfficialAccountMenuDO menuDO, Long userId, String userName) {
        if (menuDO == null) {
            throw new ValidateException("菜单信息不存在");
        }
        if (menuDO.getMenuPid() == null) {
            throw new ValidateException("上级菜单不能为空");
        }
        if (menuDO.getMenuLevel() == null) {
            throw new ValidateException("菜单等级不能为空");
        }
        if (StringUtils.isEmpty(menuDO.getAppId())) {
            throw new ValidateException("公众号开发者ID不能为空");
        }
        if (StringUtils.isEmpty(menuDO.getMenuName())) {
            throw new ValidateException("菜单名称不能为空");
        }
        if (StringUtils.isEmpty(menuDO.getMenuType())) {
            throw new ValidateException("菜单类别不能为空");
        }
        if (menuDO.getSort() == null) {
            throw new ValidateException("排序值不能为空");
        }

        if (Arrays.asList(MenuTypeEnum.CLICK.getType(),
                MenuTypeEnum.SCAN_CODE_PUSH.getType(),
                MenuTypeEnum.SCAN_CODE_WAITMSG.getType(),
                MenuTypeEnum.PIC_SYS_PHOTO.getType(),
                MenuTypeEnum.PIC_PHOTO_OR_ALBUM.getType(),
                MenuTypeEnum.PIC_WEI_XIN.getType(),
                MenuTypeEnum.LOCATION_SELECT.getType()).contains(menuDO.getMenuType())) {
            if (StringUtils.isEmpty(menuDO.getKeyword())) {
                throw new ValidateException("关键字不能为空");
            }
        } else if (MenuTypeEnum.VIEW.getType().equals(menuDO.getMenuType())) {
            if (StringUtils.isEmpty(menuDO.getUrl())) {
                throw new ValidateException("跳转地址不能为空");
            }
        } else if (MenuTypeEnum.MINI_PROGRAM.getType().equals(menuDO.getMenuType())) {
            if (StringUtils.isEmpty(menuDO.getUrl())) {
                throw new ValidateException("跳转地址不能为空");
            }
            if (StringUtils.isEmpty(menuDO.getMiniProgramAppId())) {
                throw new ValidateException("小程序appId不能为空");
            }
            if (StringUtils.isEmpty(menuDO.getPagePath())) {
                throw new ValidateException("小程序页面路径不能为空");
            }
        } else if (Arrays.asList(MenuTypeEnum.MEDIA_ID.getType(),
                MenuTypeEnum.VIEW_LIMITED.getType()).contains(menuDO.getMenuType())) {
            if (menuDO.getMsgId() == null) {
                throw new ValidateException("消息Id不能为空");
            }
        }

        menuDO.setCreatedon(new Date());
        menuDO.setCreator(userName);
        menuDO.setCreatorId(userId);
        menuDO.setIsDel(0);
        menuDO.setIsEnable(0);

        wechatOfficialAccountMenuMapper.insertOne(menuDO);
    }

    /**
     * 菜单禁用启用
     *
     * @param appId
     * @param menuId
     * @param isEnable
     * @param userId
     * @param userName
     * @return void
     * @author zhou.xy
     * @date 2019/8/30
     * @since 1.0
     */
    public void enableMenu(String appId, Long menuId, Integer isEnable, Long userId, String userName) {
        if (StringUtils.isEmpty(appId)) {
            throw new ValidateException("公众号开发者ID不能为空");
        }
        if (menuId == null) {
            throw new ValidateException("菜单ID不能为空");
        }
        if (isEnable == null || !Arrays.asList(0, 1).contains(isEnable)) {
            throw new ValidateException("未知的操作类型");
        }
        WechatOfficialAccountMenuDO query = new WechatOfficialAccountMenuDO();
        query.setMenuId(menuId);
        query.setAppId(appId);
        query.setIsDel(0);
        List<WechatOfficialAccountMenuDO> menuList = wechatOfficialAccountMenuMapper.query(query);
        if (CollectionUtils.isEmpty(menuList)) {
            throw new ValidateException("菜单不存在");
        }
        WechatOfficialAccountMenuDO menuDO = menuList.get(0);
        if (isEnable == 1) {
            if (menuDO.getIsEnable() == 1) {
                throw new ValidateException("菜单已经是启用状态");
            }
            // 启用时，需要判断对应菜单级别已启用个数，一级菜单最多三个，二级菜单最多5个
            Integer limit = 3;
            String menuLevelStr = "一级";
            if (menuDO.getMenuLevel() == 2) {
                limit = 5;
                menuLevelStr = "二级";
            }
            Example example = new Example(WechatOfficialAccountMenuDO.class);
            example.createCriteria().andEqualTo("isDel", 0)
                    .andEqualTo("appId", appId)
                    .andEqualTo("menuLevel", menuDO.getMenuLevel())
                    .andEqualTo("isEnable", 1);
            int enableCount = wechatOfficialAccountMenuMapper.selectCountByExample(example);
            if (enableCount >= limit) {
                throw new ValidateException("启用" + menuLevelStr + "菜单数量已达上限！");
            }
            menuDO.setIsEnable(1);
        } else {
            if (menuDO.getIsEnable() == 0) {
                throw new ValidateException("菜单已经是禁用状态");
            }
            menuDO.setIsEnable(0);
        }
        menuDO.setModifiedon(new Date());
        menuDO.setModifierId(userId);
        menuDO.setModifier(userName);
        wechatOfficialAccountMenuMapper.update(menuDO);
    }

    /**
     * 发送模板消息
     *
     * @param openId
     * @param templateId
     * @param jumpUrl
     * @param title
     * @param name
     * @param pName
     * @param date
     * @param remark
     * @return com.alibaba.fastjson.JSONObject
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public JSONObject sendTemplateMsg(String openId, String templateId, String jumpUrl,
                                      String title, String name,
                                      String pName, String date, String remark) {
        WeChatTemplateMsg templateMsg = new WeChatTemplateMsg();
        templateMsg.setTemplate_id(templateId);
        templateMsg.setTouser(openId);
        if (StringUtils.hasText(jumpUrl)) {
            templateMsg.setUrl(jumpUrl);
        }
        Map<String, WeChatTemplateData> templateData = new HashMap<>();
        WeChatTemplateData result = new WeChatTemplateData();
        result.setValue(title);
        result.setColor("#173177");
        templateData.put("first", result);
        WeChatTemplateData userName = new WeChatTemplateData();
        userName.setValue(name);
        userName.setColor("#173177");
        templateData.put("keyword1", userName);
        WeChatTemplateData productName = new WeChatTemplateData();
        productName.setValue(pName);
        productName.setColor("#373277");
        templateData.put("keyword2", productName);
        WeChatTemplateData d = new WeChatTemplateData();
        d.setValue(date);
        templateData.put("keyword3", d);
        WeChatTemplateData remarks = new WeChatTemplateData();
        remarks.setValue(remark);
        templateData.put("remark", remarks);

        templateMsg.setData(templateData);

        return weChatOfficialAccountApiService.sendTemplateMsg(templateMsg);
    }

    /**
     * 获取微信用户信息（本地），有缓存，有效时长2h
     *
     * @param openId
     * @return com.example.demo.model.WechatOfficialAccountUserDO
     * @author zhou.xy
     * @date 2019/8/30
     * @since 1.0
     */
    public WechatOfficialAccountUserDO queryUser(String openId) {
        String key = WECHAT_USER_INFO_CACHE_PREFIX + openId;
        WechatOfficialAccountUserDO userDO = RedisTemplateUtil.get(key);
        if (userDO == null) {
            userDO = wechatOfficialAccountUserMapper.selectByPrimaryKey(openId);
            if (userDO != null) {
                RedisTemplateUtil.set(key, userDO, 2L * 3600);
            }
        }
        return userDO;
    }
}
