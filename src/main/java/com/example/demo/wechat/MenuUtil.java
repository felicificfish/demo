package com.example.demo.wechat;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.WechatMenuDO;
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
    public static String prepareMenus(List<WechatMenuDO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return "error";
        }
        List<Object> menulists = new ArrayList<>();
        for (WechatMenuDO menu : menus) {
            if (menu.getMenuPid().equals(0L)) {
                // 一级菜单
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("pMenu", menu);
                menulists.add(menuMap);
            }
        }

        for (Object object : menulists) {
            Map<String, Object> maps = (Map<String, Object>) object;
            List<WechatMenuDO> cMenu = new ArrayList<>();
            WechatMenuDO pMenu = (WechatMenuDO) maps.get("pMenu");
            for (WechatMenuDO menu : menus) {
                Boolean bool = menu.getMenuPid().equals(pMenu.getMenuId() + "");
                System.out.println(bool);
                if (menu.getMenuPid().equals(pMenu.getMenuId() + "")) {
                    cMenu.add(menu);
                }
            }
            maps.put("cMenu", cMenu);
        }


        Map<String, Object> jsonMap = new HashMap<>();
        List<Object> jsonMapList = new ArrayList<>();
        for (Object object : menulists) {
            Map<String, Object> maps = (Map<String, Object>) object;
            WechatMenuDO pMenu = (WechatMenuDO) maps.get("pMenu");
            List<WechatMenuDO> cMenu = (List<WechatMenuDO>) maps.get("cMenu");

            if (cMenu.size() > 0) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                for (WechatMenuDO wxMenu : cMenu) {
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
    private static Map<String, Object> getMenuMap(WechatMenuDO menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", menu.getMenuName());
        map.put("type", menu.getMenuType());
        if ("click".equals(menu.getMenuType())) {
            // 事件菜单
            if ("fix".equals(menu.getEventType())) {
                // fix 消息
                // 以 _fix_ 开头
                map.put("key", "_fix_" + menu.getMsgId());
            } else {
                if (StringUtils.isEmpty(menu.getInputCode())) {
                    // 如果inputcode 为空，默认设置为 subscribe，以免创建菜单失败
                    map.put("key", "subscribe");
                } else {
                    map.put("key", menu.getInputCode());
                }
            }
        } else {
            // 链接菜单-view
            map.put("url", menu.getUrl());
        }
        return map;
    }

    private static Map<String, Object> getParentMenuMap(WechatMenuDO menu, List<Map<String, Object>> mapList) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", menu.getMenuName());
        map.put("sub_button", mapList);
        return map;
    }

}
