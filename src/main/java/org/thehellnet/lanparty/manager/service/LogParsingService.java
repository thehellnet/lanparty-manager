package org.thehellnet.lanparty.manager.service;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.exception.LanPartyException;
import org.thehellnet.lanparty.manager.model.logline.line.LogLine;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.settings.JmsSettings;
import org.thehellnet.lanparty.manager.utility.logline.LogLineParser;
import org.thehellnet.lanparty.manager.utility.logline.LogLineParserFactory;

import javax.jms.JMSException;

@Service
public class LogParsingService {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingService.class);

    private final ServerRepository serverRepository;

    @Autowired
    public LogParsingService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @JmsListener(destination = JmsSettings.JMS_PATH_LOG_PARSING)
    public void parseLogLine(final Message<ServerLogLine> message) throws JMSException {
        ServerLogLine serverLogLine = message.getPayload();

        String rawLogLine = serverLogLine.getLine();

        Server server = serverRepository.findById(serverLogLine.getThnOlgServer().getId()).orElseThrow();
        Game game = server.getGame();

        DateTime dateTime = DateTime.now();

        LogLineParser logLineParser = LogLineParserFactory.getLogLineParser(game, rawLogLine);
        LogLine logLine;

        try {
            logLine = logLineParser.parse(dateTime);
        } catch (LanPartyException e) {
            logger.warn("Unable to parse log line: {}", rawLogLine, e);
            return;
        }

        logger.debug("New server logLine from {}, {}", server, logLine);
    }
}
