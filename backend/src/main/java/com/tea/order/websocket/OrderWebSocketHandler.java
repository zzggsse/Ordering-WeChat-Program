package com.tea.order.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public OrderWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("WS connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        log.info("WS closed: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 简单 echo，便于联调
        sendToAll(message.getPayload());
    }

    /** 向所有已连接客户端广播一条消息（订单状态变化、叫号等） */
    public void broadcast(String payload) {
        TextMessage msg = new TextMessage(payload);
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(msg);
                } catch (IOException e) {
                    log.warn("WS send failed: {}", e.getMessage());
                }
            }
        }
    }

    public void broadcast(Object event) {
        try {
            broadcast(objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            log.warn("WS serialize event failed: {}", e.getMessage());
        }
    }

    private void sendToAll(String payload) {
        TextMessage msg = new TextMessage(payload);
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(msg);
                } catch (IOException e) {
                    log.warn("WS send failed: {}", e.getMessage());
                }
            }
        }
    }
}
