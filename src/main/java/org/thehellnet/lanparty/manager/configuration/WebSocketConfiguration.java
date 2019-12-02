package org.thehellnet.lanparty.manager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.thehellnet.lanparty.manager.api.v1.ws.ShowcaseWSHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final ShowcaseWSHandler showcaseWSHandler;

    public WebSocketConfiguration(ShowcaseWSHandler showcaseWSHandler) {
        this.showcaseWSHandler = showcaseWSHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(showcaseWSHandler, ShowcaseWSHandler.URL)
                .setAllowedOrigins("*");
    }
}
