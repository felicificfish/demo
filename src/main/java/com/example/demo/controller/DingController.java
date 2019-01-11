package com.example.demo.controller;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 钉钉机器人
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@RestController
public class DingController {
    private final static String WEBHOOK = "https://oapi.dingtalk.com/robot/send?access_token=73fc74f81d5398cc5ed87240ef817e35916e27e8ba4231ff4caea96933b5a891";
    private DingtalkChatbotClient client = new DingtalkChatbotClient();

    /**
     * 纯文本发送
     *
     * @param text    发送文本
     * @param mobiles @用户，以英文半角逗号隔开','，如：15869165606,13335712772
     * @param isAtAll 是否@所有用户
     * @return
     */
    @PostMapping(value = "/ding/text")
    public SendResult text(String text, String mobiles, boolean isAtAll) {
        TextMessage message = new TextMessage(text);
        List<String> mobileList = Arrays.asList("15869165606", "13335712772", "19884176007");
        message.setAtMobiles(mobileList);
        SendResult result = null;
        try {
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param message title       消息列表中展示的标题
     *                bannerUrl   图片地址
     *                briefTitle  标题
     *                briefText   概述
     *                singleTitle
     *                singleURL   跳转地址
     *                hideAvatar  是否隐藏发送人
     * @return
     */
    @PostMapping(value = "/ding/single")
    public SendResult single(SingleTargetActionCardMessage message) {
        SendResult result = null;
        try {
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping(value = "/ding/markdown")
    public SendResult markdown(MarkdownMessage message) {
        SendResult result = null;
        try {
            message.setTitle("This is a markdown message");

            message.add(MarkdownMessage.getHeaderText(1, "header 1"));
            message.add(MarkdownMessage.getHeaderText(2, "header 2"));
            message.add(MarkdownMessage.getHeaderText(3, "header 3"));
            message.add(MarkdownMessage.getHeaderText(4, "header 4"));
            message.add(MarkdownMessage.getHeaderText(5, "header 5"));
            message.add(MarkdownMessage.getHeaderText(6, "header 6"));

            message.add(MarkdownMessage.getReferenceText("reference text"));
            message.add("\n\n");

            message.add("normal text");
            message.add("\n\n");

            message.add(MarkdownMessage.getBoldText("Bold Text"));
            message.add("\n\n");

            message.add(MarkdownMessage.getItalicText("Italic Text"));
            message.add("\n\n");

            ArrayList<String> orderList = new ArrayList<String>();
            orderList.add("order item1");
            orderList.add("order item2");
            message.add(MarkdownMessage.getOrderListText(orderList));
            message.add("\n\n");

            ArrayList<String> unorderList = new ArrayList<String>();
            unorderList.add("unorder item1");
            unorderList.add("unorder item2");
            message.add(MarkdownMessage.getUnorderListText(unorderList));
            message.add("\n\n");

            message.add(MarkdownMessage.getImageText("http://img01.taobaocdn.com/top/i1/LB1GCdYQXXXXXXtaFXXXXXXXXXX"));
            message.add(MarkdownMessage.getLinkText("This is a link", "dtmd://dingtalkclient/sendMessage?content=linkmessage"));
            message.add(MarkdownMessage.getLinkText("中文跳转", "dtmd://dingtalkclient/sendMessage?content=" + URLEncoder.encode("链接消息", "UTF-8")));
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param message title      标题
     *                text       内容
     *                picUrl     图片地址
     *                messageUrl 跳转地址
     * @return
     */
    @PostMapping(value = "/ding/link")
    public SendResult link(LinkMessage message) {
        SendResult result = null;
        try {
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 图集
     *
     * @return
     */
    @PostMapping(value = "/ding/feedcard")
    public SendResult feedCard() {
        SendResult result = null;
        FeedCardMessage message = new FeedCardMessage();

        List<FeedCardMessageItem> items = new ArrayList<FeedCardMessageItem>();
        FeedCardMessageItem item1 = new FeedCardMessageItem();
        item1.setTitle("心灵鸡汤1");
        item1.setPicURL("https://img.alicdn.com/tps/TB1XLjqNVXXXXc4XVXXXXXXXXXX-170-64.png");
        item1.setMessageURL("http://www.dingtalk.com");
        items.add(item1);

        FeedCardMessageItem item2 = new FeedCardMessageItem();
        item2.setTitle("心灵鸡汤2");
        item2.setPicURL("https://img.alicdn.com/tps/TB1XLjqNVXXXXc4XVXXXXXXXXXX-170-64.png");
        item2.setMessageURL("http://www.dingtalk.com");
        items.add(item2);

        FeedCardMessageItem item3 = new FeedCardMessageItem();
        item3.setTitle("心灵鸡汤3");
        item3.setPicURL("https://img.alicdn.com/tps/TB1XLjqNVXXXXc4XVXXXXXXXXXX-170-64.png");
        item3.setMessageURL("http://www.dingtalk.com");
        items.add(item3);

        message.setFeedItems(items);

        try {
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping(value = "/ding/actioncard")
    public SendResult actionCard() {
        SendResult result = null;

        ActionCardMessage message = new ActionCardMessage();
        message.setBriefText("亲，小秘没有看懂你的问题哦，换个说法问问小秘看~你也可以试试以下问题");
        ActionCardAction action1 = new ActionCardAction("考勤打卡", "http://www.dingtalk.com");
        message.addAction(action1);
        ActionCardAction action2 = new ActionCardAction("办公电话", "http://www.dingtalk.com");
        message.addAction(action2);
        ActionCardAction action3 = new ActionCardAction("智能客服", "http://www.dingtalk.com");
        message.addAction(action3);
        ActionCardAction action4 = new ActionCardAction("更多问题", "http://www.dingtalk.com");
        message.addAction(action4);

        try {
            result = client.send(WEBHOOK, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
}
