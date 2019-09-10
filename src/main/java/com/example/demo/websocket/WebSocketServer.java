package com.example.demo.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * websocket服务
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    /**
     * 记录当前在线连接数，应该把它设计成线程安全的
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArrayList<WebSocketServer> webSocketSet = new CopyOnWriteArrayList<>();
    /**
     * 根据userId来获取对应的WebSocket
     */
//    private static ConcurrentHashMap<String, WebSocketServer> websocketList = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private String sid = "";

    /**
     * 群发自定义消息
     *
     * @param message
     * @param sid
     * @return void
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口：{}，推送内容：{}", sid, message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听：{}，当前在线人数为：{}", sid, getOnlineCount());

        this.sid = sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO 异常{}", e.getMessage());
        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一个连接关闭！当前在线人数为：{}", getOnlineCount());
    }

    /**
     * 接收客户端消息
     *
     * @param message
     * @param session
     * @return void
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口：{}的消息：{}", sid, message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("websocket IO 异常{}", e.getMessage());
            }
        }
    }

    @OnError
    public void onError(Session sessoin, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
