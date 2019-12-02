package org.thehellnet.lanparty.manager.api.v1.ws;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thehellnet.lanparty.manager.exception.showcase.TagNotPresentException;
import org.thehellnet.lanparty.manager.service.ShowcaseService;

import java.util.List;

@Component
public class ShowcaseWSHandler extends TextWebSocketHandler {

    private static final String TAG_HEADER = "X-Showcase-Tag";

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
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String tag = getTag(session);
        showcaseService.removeShowcase(tag);
    }

    private String getTag(WebSocketSession session) {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();

        if (!handshakeHeaders.containsKey(TAG_HEADER)) {
            throw new TagNotPresentException();
        }

        List<String> tagHeaders = handshakeHeaders.get(TAG_HEADER);
        if (tagHeaders == null || tagHeaders.size() == 0) {
            throw new TagNotPresentException();
        }

        return tagHeaders.get(0);
    }

}
