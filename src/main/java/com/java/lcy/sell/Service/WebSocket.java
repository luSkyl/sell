package com.java.lcy.sell.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/webSocket")//它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址。
@Slf4j
public class WebSocket {
    private Session session;

    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        clients.put(session.getId(), session);
        log.info("【WebSocket消息】 有新的连接 总数:{}",clients.size());
    }

    @OnClose
    public void onClose(Session session){
        clients.remove(session.getId());
        log.info("【WebSocket消息】 连接断开 总数:{}",clients.size());
    }


    @OnMessage
    public void onMessage(String message){
        this.sendMessage(message);
        log.info("【WebSocket消息】 收到客户端发来的消息:{}",message);
    }

    public void sendMessage(String message){
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            log.info("【WebSocket消息】 广播消息 , message={}",message);
            sessionEntry.getValue().getAsyncRemote().sendText(message);
        }

    }

}
