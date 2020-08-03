package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;

import javax.jms.JMSException;

@Service
public class LogParsingService {

    public static final String QUEUE_NAME_SERVER_LOGLINE = "server-logline-response-queue";

    private static final Logger logger = LoggerFactory.getLogger(LogParsingService.class);

    @JmsListener(destination = QUEUE_NAME_SERVER_LOGLINE)
    public void parseLogLine(final Message<ServerLogLine> message) throws JMSException {
        ServerLogLine serverLogLine = message.getPayload();
        logger.info("New server logLine from {}, {}", serverLogLine.getThnOlgServer(), serverLogLine.getLine());
    }
}
