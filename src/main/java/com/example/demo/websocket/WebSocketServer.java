package com.example.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.LeaveMessageDO;
import com.example.demo.utils.SpringContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket服务
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {
    /**
     * 记录当前在线连接数，应该把它设计成线程安全的
     */
    private static int onlineCount = 0;
    /**
     * 根据userId来获取对应的WebSocket
     */
    private static ConcurrentHashMap<String, WebSocketServer> websocketList = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private String userId = "";

    @Resource
    private ThreadPoolTaskExecutor asyncRequestThreadPool;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    @OnError
    public void onError(Session sessoin, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 连接调用
     *
     * @param session
     * @param userId  用户ID，不传时取到的值为null
     * @return void
     * @author zhou.xy
     * @date 2019/9/11
     * @since 1.0
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        websocketList.put(userId, this);
        // 在线人数+1
        addOnlineCount();
        log.info("有新窗口开始监听：{}，当前在线人数为：{}", userId, getOnlineCount());

        this.userId = userId;
        LeaveMessageDO msg = new LeaveMessageDO();
        msg.setMessageId(0L);
        msg.setMessage("欢迎加入");
        sendMessage(JSON.toJSONString(msg));
    }

    /**
     * 连接关闭调用
     *
     * @param
     * @return void
     * @author zhou.xy
     * @date 2019/9/11
     * @since 1.0
     */
    @OnClose
    public void onClose() {
        if (websocketList.get(this.userId) != null) {
            websocketList.remove(this.userId);
            // 在线人数-1
            subOnlineCount();
            log.info("有一个连接关闭！当前在线人数为：{}", getOnlineCount());
        }
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
        log.info("收到来自窗口：{}的消息：{}", userId, message);
        if (StringUtils.isEmpty(message)) {
            return;
        }
        LeaveMessageDO messageDO = JSON.parseObject(message, LeaveMessageDO.class);
        messageDO.setOpt(LeaveMessageDO.OPT_SEND);
        message = JSON.toJSONString(messageDO);
        if (asyncRequestThreadPool == null) {
            asyncRequestThreadPool = SpringContext.getBean(ThreadPoolTaskExecutor.class);
        }
        for (Map.Entry<String, WebSocketServer> item : websocketList.entrySet()) {
            String finalMessage = message;
            asyncRequestThreadPool.execute(() -> item.getValue().sendMessage(finalMessage));
        }
    }

    /**
     * 服务器主动推送
     *
     * @param message
     * @return void
     * @author zhou.xy
     * @date 2019/9/11
     * @since 1.0
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("websocket IO 异常{}", e.getMessage());
        }
    }

    /**
     * 群发自定义消息
     *
     * @param message 消息内容
     * @param userId  接收对象，为null时发送所有客户端
     * @return void
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public void sendInfo(String message, String userId) throws IOException {
        log.info("推送消息到窗口：{}，推送内容：{}", userId == null ? "全部" : userId, message);
        for (Map.Entry<String, WebSocketServer> item : websocketList.entrySet()) {
            // 这里可以设定只推送给这个sid的，为null则全部推送
            if (userId == null) {
                asyncRequestThreadPool.execute(() -> item.getValue().sendMessage(message));
            } else if (item.getKey().equals(userId)) {
                asyncRequestThreadPool.execute(() -> item.getValue().sendMessage(message));
                break;
            }
        }
    }
}
