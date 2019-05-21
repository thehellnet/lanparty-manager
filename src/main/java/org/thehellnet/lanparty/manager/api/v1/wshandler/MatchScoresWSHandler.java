package org.thehellnet.lanparty.manager.api.v1.wshandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.HashMap;
import java.util.Map;

@Service
public class MatchScoresWSHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MatchScoresWSHandler.class);

    private Map<Tournament, WebSocketSession> wsSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("Connection Established");

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        logger.info("Connection Closed");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        logger.info("handleTextMessage: {}", message.getPayload());
    }
}
