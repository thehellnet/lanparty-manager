package org.thehellnet.lanparty.manager.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;

public class TestMessageBuilder<T> {

    private final T payload;

    private TestMessageBuilder(T payload) {
        this.payload = payload;
    }

    public static Message<?> generateMessage(ServerLogLine payload) {
        return new TestMessageBuilder<ServerLogLine>(payload).generate();
    }

    public Message<T> generate() {
        return new Message<>() {
            @Override
            public T getPayload() {
                return payload;
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
    }
}
