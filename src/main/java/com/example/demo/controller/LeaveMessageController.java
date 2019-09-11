package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.LeaveMessageDO;
import com.example.demo.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * 留言
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@RestController
public class LeaveMessageController {
    @Autowired
    WebSocketServer webSocketServer;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping(value = "/socket/msg")
    public void leaveMessage(LeaveMessageDO message) {
        try {
            message.setOpt(LeaveMessageDO.OPT_SEND);
            message.setSendTime(new Date());
            webSocketServer.sendInfo(JSON.toJSONString(message), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/socket/msg/withdraw")
    public void withdrawMessage(Long messageId) {
        try {
            LeaveMessageDO message = new LeaveMessageDO();
            message.setMessageId(messageId);
            message.setOpt(LeaveMessageDO.OPT_WITHDRAW);
            webSocketServer.sendInfo(JSON.toJSONString(message), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
