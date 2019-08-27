package com.example.demo.controller;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.wechat.WeChatMsgUtil;
import com.example.demo.wechat.WeChatResponseMsgService;
import com.example.demo.wechat.WeChatService;
import com.example.demo.wechat.WeChatUtil;
import com.example.demo.wechat.model.WeChatInputMsg;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Value("${wechat.appId}")
    public String appId;
    @Value("${wechat.appSecret}")
    public String appSecret;
    @Value("${wechat.token}")
    public String wechatToken;

    @Autowired
    private WeChatResponseMsgService weChatResponseMsgService;
    @Autowired
    private WeChatService weChatService;

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
     * @param publicAccount
     * @return void
     * @author zhou.xy
     * @date 2019/8/27
     * @since 1.0
     */
    @GetMapping(value = "/wechat/menu/publish")
    public void publishMenu(String publicAccount) {
        if (StringUtils.isEmpty(publicAccount)) {
            throw new ValidateException("公众账号不能为空");
        }
        weChatService.publishMenu(publicAccount, 1L, "admin");
    }
}
