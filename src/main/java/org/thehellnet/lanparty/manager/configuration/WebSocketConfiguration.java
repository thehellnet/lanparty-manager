package org.thehellnet.lanparty.manager.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.thehellnet.lanparty.manager.api.v1.wshandler.MatchScoresWSHandler;

@Configuration
@EnableWebSocket
@ComponentScan(basePackages = "org.thehellnet.lanparty.manager.api")
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final MatchScoresWSHandler apiV1MatchScoresWSHandler;

    public WebSocketConfiguration(MatchScoresWSHandler apiV1MatchScoresWSHandler) {
        this.apiV1MatchScoresWSHandler = apiV1MatchScoresWSHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(apiV1MatchScoresWSHandler, "/match/scores")
                .setAllowedOrigins("*");
    }
}
