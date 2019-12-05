package org.thehellnet.lanparty.manager.api.v1.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thehellnet.lanparty.manager.exception.showcase.TagNotPresentException;
import org.thehellnet.lanparty.manager.service.ShowcaseService;

import java.net.URI;
import java.util.Objects;

@Component
public class ShowcaseWSHandler extends TextWebSocketHandler {

    public static final String URL_PREFIX = "/api/public/ws/showcase";
    public static final String URL = URL_PREFIX + "/**";

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseWSHandler.class);

    private final ShowcaseService showcaseService;

    public ShowcaseWSHandler(ShowcaseService showcaseService) {
        this.showcaseService = showcaseService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String tag = getTag(session);
        showcaseService.addShowcase(tag, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String tag = getTag(session);
        String messagePayload = message.getPayload();
        showcaseService.parseMessage(tag, messagePayload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String tag = getTag(session);
        showcaseService.removeShowcase(tag);
    }

    private String getTag(WebSocketSession session) {
        URI sessionUri = session.getUri();
        String url = Objects.requireNonNull(sessionUri).getPath();
        String tag = url.split(URL_PREFIX)[1].substring(1);

        if (tag.length() == 0) {
            throw new TagNotPresentException();
        }

        return tag;
    }
}
