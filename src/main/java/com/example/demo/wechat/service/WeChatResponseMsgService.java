package com.example.demo.wechat.service;

import com.example.demo.wechat.model.WeChatInputMsg;
import com.example.demo.wechat.model.WeChatTextMsg;
import com.example.demo.wechat.utils.WeChatMsgUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 微信消息服务处理
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Log4j2
@Service
public class WeChatResponseMsgService {
    public static final String WELCOME_MSG = "您好，感谢您的关注！";
    @Autowired
    private WeChatOfficialAccountApiService weChatOfficialAccountApiService;

    public String processMsg(WeChatInputMsg inputMsg) {
        String msgType = inputMsg.getMsgType();
        if (Objects.equals(WeChatMsgUtil.REQ_MESSAGE_TYPE_TEXT, msgType)) {
            return processTextMsg(inputMsg, WELCOME_MSG);
        } else if (Objects.equals(WeChatMsgUtil.REQ_MESSAGE_TYPE_EVENT, msgType)) {
            return processEventMsg(inputMsg);
        }
        return null;
    }

    /**
     * 处理文本消息
     *
     * @param inputMessage 用户输入信息
     * @return
     */
    private String processTextMsg(WeChatInputMsg inputMessage, String welcome) {
        // TODO 改成动态配置
        if (StringUtils.hasText(inputMessage.getContent())) {
            WeChatTextMsg responseText = new WeChatTextMsg();
            responseText.setToUserName(inputMessage.getFromUserName());
            responseText.setFromUserName(inputMessage.getToUserName());
            responseText.setMsgType(WeChatMsgUtil.REQ_MESSAGE_TYPE_TEXT);
            responseText.setCreateTime(System.currentTimeMillis());
            responseText.setContent(welcome);
            return WeChatMsgUtil.textMessageToXml(responseText);
        }
        return null;
    }

    /**
     * 处理事件消息
     *
     * @param inputMessage
     * @return
     */
    private String processEventMsg(WeChatInputMsg inputMessage) {
        String response = "";
        String key = inputMessage.getEventKey();
        if (Objects.equals(WeChatMsgUtil.EVENT_TYPE_SUBSCRIBE, inputMessage.getEvent())) {
            // 关注公众号
            String openId = inputMessage.getFromUserName();
            if (!StringUtils.isEmpty(openId)) {
                weChatOfficialAccountApiService.getUserInfoAndSave(openId);
            }
            inputMessage.setContent(WELCOME_MSG);
            response = processTextMsg(inputMessage, WELCOME_MSG);
        } else if (Objects.equals(WeChatMsgUtil.EVENT_TYPE_UNSUBSCRIBE, inputMessage.getEvent())) {
            // TODO 取消订阅消息
            response = "OK";
        } else {
            // TODO 点击事件消息
            if (!StringUtils.isEmpty(key)) {
                switch (key.toUpperCase()) {
                    case "LIKE":
                        inputMessage.setContent("感谢您的支持！");
                        response = processTextMsg(inputMessage, "感谢您的支持！");
                        break;
                }
            }
        }
        return response;
    }
}
