package com.example.demo.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.dao.WechatOfficialAccountMenuMapper;
import com.example.demo.model.WechatOfficialAccountMenuDO;
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
    @Autowired
    private WechatOfficialAccountMenuMapper wechatOfficialAccountMenuMapper;
    @Autowired
    private WeChatOfficialAccountApiService weChatOfficialAccountApiService;

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
}
