package com.example.demo.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.WechatOfficialAccountMenuDO;
import com.example.demo.wechat.constant.MenuTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 微信公众号菜单工具类
 *
 * @author zhou.xy
 * @since 2019/8/27
 */
public class MenuUtil {
    /**
     * 封装微信公众账号的菜单
     *
     * @param menus 菜单列表
     * @return
     */
    public static String prepareMenus(List<WechatOfficialAccountMenuDO> menus) {
        WechatOfficialAccountMenuDO parent = new WechatOfficialAccountMenuDO();
        parent.setMenuId(0L);
        recursiveProcessing(parent, menus);

        JSONObject menuInfo = new JSONObject();

        List<WechatOfficialAccountMenuDO> menuList = parent.getChildren();
        if (CollectionUtils.isEmpty(menuList)) {
            menuInfo.put("button", Collections.EMPTY_LIST);
            return menuInfo.toJSONString();
        }

        List<JSONObject> buttons = new ArrayList<>();
        for (WechatOfficialAccountMenuDO menu : menuList) {
            JSONObject button = new JSONObject();
            button.put("name", menu.getMenuName());

            List<WechatOfficialAccountMenuDO> children = menu.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                button.put("type", menu.getMenuType());
                buttonProcessing(menu, button);
            } else {
                List<JSONObject> subButtons = new ArrayList<>();
                for (WechatOfficialAccountMenuDO menuDO : children) {
                    JSONObject subButton = new JSONObject();
                    subButton.put("name", menuDO.getMenuName());
                    subButton.put("type", menuDO.getMenuType());
                    buttonProcessing(menuDO, subButton);
                    subButtons.add(subButton);
                }
                button.put("sub_button", subButtons);
            }
            buttons.add(button);
        }
        menuInfo.put("button", buttons);
        return JSON.toJSONString(menuInfo);

    }

    /**
     * 菜单递归处理
     *
     * @param parent
     * @param menuDOList
     * @return void
     * @author zhou.xy
     * @date 2019/8/29
     * @since 1.0
     */
    public static void recursiveProcessing(WechatOfficialAccountMenuDO parent,
                                           List<WechatOfficialAccountMenuDO> menuDOList) {
        if (CollectionUtils.isEmpty(menuDOList)) {
            return;
        }
        for (WechatOfficialAccountMenuDO menu : menuDOList) {
            if (parent.getMenuId().equals(menu.getMenuPid())) {
                List<WechatOfficialAccountMenuDO> children = parent.getChildren();
                if (CollectionUtils.isEmpty(children)) {
                    children = new ArrayList<>();
                }
                children.add(menu);
                parent.setChildren(children);

                recursiveProcessing(menu, menuDOList);
            }
        }
    }

    private static void buttonProcessing(WechatOfficialAccountMenuDO menu, JSONObject button) {
        if (Arrays.asList(MenuTypeEnum.CLICK.getType(),
                MenuTypeEnum.SCAN_CODE_PUSH.getType(),
                MenuTypeEnum.SCAN_CODE_WAITMSG.getType(),
                MenuTypeEnum.PIC_SYS_PHOTO.getType(),
                MenuTypeEnum.PIC_PHOTO_OR_ALBUM.getType(),
                MenuTypeEnum.PIC_WEI_XIN.getType(),
                MenuTypeEnum.LOCATION_SELECT.getType()).contains(menu.getMenuType())) {
            if (StringUtils.isEmpty(menu.getKeyword())) {
                // 如key为空，默认设置为 subscribe，以免创建菜单失败
                button.put("key", "subscribe");
            } else {
                button.put("key", menu.getKeyword());
            }
        } else if (MenuTypeEnum.MINI_PROGRAM.getType().equals(menu.getMenuType())) {
            button.put("url", menu.getUrl());
            button.put("appid", menu.getMiniProgramAppId());
            button.put("pagepath", menu.getPagePath());
        } else if (MenuTypeEnum.VIEW.getType().equals(menu.getMenuType())) {
            button.put("url", menu.getUrl());
        } else if (Arrays.asList(MenuTypeEnum.MEDIA_ID.getType(),
                MenuTypeEnum.VIEW_LIMITED.getType()).contains(menu.getMenuType())) {
            button.put("media_id", menu.getMsgId());
        }
    }
}
