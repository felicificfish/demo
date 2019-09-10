package com.example.demo.controller;

import com.example.demo.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 留言
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@RestController
public class LeaveMessageController {

    @GetMapping(value = "/socket/msg")
    public void leaveMessage(String msessage) {
        try {
            WebSocketServer.sendInfo(msessage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
