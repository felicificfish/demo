package com.example.demo.controller;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.model.WechatOfficialAccountMenuDO;
import com.example.demo.wechat.utils.WeChatMsgUtil;
import com.example.demo.wechat.service.WeChatResponseMsgService;
import com.example.demo.wechat.service.WeChatOfficialAccountService;
import com.example.demo.wechat.utils.WeChatUtil;
import com.example.demo.wechat.model.WeChatInputMsg;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 微信公众号
 *
 * @author zhou.xy
 * @date 2019/8/27
 * @since 1.0
 */
@Log4j2
@RestController
public class WeChatOfficialAccountController {
    @Value("${wechat.token}")
    public String wechatToken;

    @Autowired
    private WeChatResponseMsgService weChatResponseMsgService;
    @Autowired
    private WeChatOfficialAccountService weChatService;

    /**
     * 微信公众号与开发者服务器交互
     *
     * @param request
     * @param response
     * @return java.lang.String
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @GetMapping(value = "/plc/wechat/msg")
    public String weChatDoGet(HttpServletRequest request, HttpServletResponse response) {
        // 获取token，进行验证
        if (!StringUtils.isEmpty(wechatToken)) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = Objects.toString(request.getParameter("timestamp"));
            // 随机数
            String nonce = Objects.toString(request.getParameter("nonce"));
            // 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
            if (WeChatUtil.checkSignature(wechatToken, signature, timestamp, nonce)) {
                return request.getParameter("echostr");
            }
        }
        return "error";
    }

    /**
     * 微信公众号与开发者服务器交互
     *
     * @param request
     * @param response
     * @return java.lang.String
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @PostMapping(value = "/plc/wechat/msg")
    public String weChatDoPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            WeChatInputMsg weChatInputMsgDO = WeChatMsgUtil.parseXml(request.getInputStream());
            return weChatResponseMsgService.processMsg(weChatInputMsgDO);
        } catch (Exception e) {
            log.warn("微信接收客户消息处理异常", e);
        }
        return "error";
    }

    /**
     * 发布菜单
     *
     * @param appId
     * @return void
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @GetMapping(value = "/wechat/menu/publish")
    public void publishMenu(String appId) {
        if (StringUtils.isEmpty(appId)) {
            throw new ValidateException("公众号不能为空");
        }
        weChatService.publishMenu(appId, 1L, "admin");
    }

    /**
     * 获取菜单数据
     *
     * @param appId
     * @return java.util.List<com.example.demo.model.WechatOfficialAccountMenuDO>
     * @author zhou.xy
     * @date 2019/8/28
     * @since 1.0
     */
    @GetMapping(value = "/wechat/menuList")
    public List<WechatOfficialAccountMenuDO> queryMenuList(String appId) {
        if (StringUtils.isEmpty(appId)) {
            throw new ValidateException("公众号不能为空");
        }
        return weChatService.queryMenuList(appId);
    }

    /**
     * 删除菜单
     *
     * @param menuIds
     * @return void
     * @author zhou.xy
     * @date 2019/8/28
     * @since 1.0
     */
    @DeleteMapping(value = "/wechat/menu/delete")
    public void deleteMenu(String menuIds) {
        log.info("delete menuIds : {}", menuIds);
    }
}
