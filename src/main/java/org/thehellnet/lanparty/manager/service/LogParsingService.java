package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;
import org.thehellnet.lanparty.manager.settings.JmsSettings;

import javax.jms.JMSException;

@Service
public class LogParsingService {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingService.class);

    @JmsListener(destination = JmsSettings.JMS_PATH_LOG_PARSING)
    public void parseLogLine(final Message<ServerLogLine> message) throws JMSException {
        ServerLogLine serverLogLine = message.getPayload();
        logger.debug("New server logLine from {}, {}", serverLogLine.getThnOlgServer(), serverLogLine.getLine());


    }
}
