package org.thehellnet.lanparty.manager.api.v1.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ShowcaseWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseWebSocket.class);

    @MessageMapping
    public String chat(String message) {
        logger.debug("Message: {}", message);
        return String.format("Message: %s", message);
    }
}
