package com.example.demo.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.WechatOfficialAccountMenuDO;
import com.example.demo.wechat.constant.MenuTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (CollectionUtils.isEmpty(menus)) {
            return "error";
        }
        List<Object> menuList = new ArrayList<>();
        for (WechatOfficialAccountMenuDO menu : menus) {
            if (menu.getMenuPid().equals(0L)) {
                // 一级菜单
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("pMenu", menu);
                menuList.add(menuMap);
            }
        }

        for (Object object : menuList) {
            Map<String, Object> maps = (Map<String, Object>) object;
            List<WechatOfficialAccountMenuDO> cMenu = new ArrayList<>();
            WechatOfficialAccountMenuDO pMenu = (WechatOfficialAccountMenuDO) maps.get("pMenu");
            for (WechatOfficialAccountMenuDO menu : menus) {
                Boolean bool = menu.getMenuPid().equals(pMenu.getMenuId() + "");
                if (menu.getMenuPid().equals(pMenu.getMenuId() + "")) {
                    cMenu.add(menu);
                }
            }
            maps.put("cMenu", cMenu);
        }

        Map<String, Object> jsonMap = new HashMap<>();
        List<Object> jsonMapList = new ArrayList<>();
        for (Object object : menuList) {
            Map<String, Object> maps = (Map<String, Object>) object;
            WechatOfficialAccountMenuDO pMenu = (WechatOfficialAccountMenuDO) maps.get("pMenu");
            List<WechatOfficialAccountMenuDO> cMenu = (List<WechatOfficialAccountMenuDO>) maps.get("cMenu");

            if (cMenu.size() > 0) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                for (WechatOfficialAccountMenuDO wxMenu : cMenu) {
                    mapList.add(getMenuMap(wxMenu));
                }
                jsonMapList.add(getParentMenuMap(pMenu, mapList));
            } else {
                jsonMapList.add(getMenuMap(pMenu));
            }
        }
        jsonMap.put("button", jsonMapList);

        return JSON.toJSONString(jsonMap);

    }


    /**
     * 此方法是构建菜单对象的；构建菜单时，对于  key 的值可以任意定义；
     * 当用户点击菜单时，会把key传递回来；对已处理就可以了
     *
     * @param menu
     * @return
     */
    private static Map<String, Object> getMenuMap(WechatOfficialAccountMenuDO menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", menu.getMenuName());
        map.put("type", menu.getMenuType());
        if (MenuTypeEnum.CLICK.getType().equals(menu.getMenuType())) {
            // 事件菜单
            if ("fix".equals(menu.getEventType())) {
                // fix 消息
                // 以 _fix_ 开头
                map.put("key", "_fix_" + menu.getMsgId());
            } else {
                if (StringUtils.isEmpty(menu.getKeyword())) {
                    // 如果inputcode 为空，默认设置为 subscribe，以免创建菜单失败
                    map.put("key", "subscribe");
                } else {
                    map.put("key", menu.getKeyword());
                }
            }
        } else if ("miniprogram".equals(menu.getMenuType())) {
            map.put("url", menu.getUrl());
            map.put("appid", menu.getMiniProgramAppId());
            map.put("pagepath", menu.getPagePath());
        } else if ("scancode_push".equals(menu.getMenuType())) {
            map.put("key", menu.getKeyword());
        } else {
            // 链接菜单-view
            map.put("url", menu.getUrl());
        }
        return map;
    }

    private static Map<String, Object> getParentMenuMap(WechatOfficialAccountMenuDO menu, List<Map<String, Object>> mapList) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", menu.getMenuName());
        map.put("sub_button", mapList);
        return map;
    }

}
