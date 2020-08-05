package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.service.ServerService;
import org.thehellnet.lanparty.manager.settings.JmsSettings;
import org.thehellnet.utility.log.LogTailer;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LogParsingRunner extends AbstractRunner implements Runner {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingRunner.class);

    private final ServerService serverService;
    private final ConnectionFactory connectionFactory;

    private final Map<Server, LogTailer> logTailers = new HashMap<>();

    private JmsTemplate jmsTemplate;

    @Autowired
    public LogParsingRunner(ServerService serverService, ConnectionFactory connectionFactory) {
        this.serverService = serverService;
        this.connectionFactory = connectionFactory;

        initJmsTemplate();
    }

    @Override
    protected void startRunner() {
        logger.info("START");

        List<Server> serverList = serverService.getLogEnabledServers();

        for (Server thnOlgServer : serverList) {
            logger.debug("Starting LogTailer for server \"{}\": {}", thnOlgServer, thnOlgServer.getLogFile());

            if (thnOlgServer.getLogFile() == null || thnOlgServer.getLogFile().length() == 0) {
                logger.warn("Empty logFile path for server {}", thnOlgServer);
                continue;
            }

            File logFile = new File(thnOlgServer.getLogFile());
            LogTailer logTailer = new LogTailer(logFile, line -> sendServerLogLineMessage(thnOlgServer, line));
            logTailer.start();

            logger.debug("Adding LogTailer for server {}", thnOlgServer);
            logTailers.put(thnOlgServer, logTailer);
        }
    }

    @Override
    protected void stopRunner() {
        logger.info("STOP");

        Set<Server> servers = logTailers.keySet();
        Server[] thnOlgServers = servers.toArray(new Server[0]);

        for (Server thnOlgServer : thnOlgServers) {
            LogTailer logTailer = logTailers.get(thnOlgServer);
            logTailer.stop();
            logTailer.join();

            logger.debug("Removing LogTailer for server {}", thnOlgServer);
            logTailers.remove(thnOlgServer);
        }
    }

    private void initJmsTemplate() {
        jmsTemplate = new JmsTemplate(this.connectionFactory);
        jmsTemplate.setDefaultDestinationName(JmsSettings.JMS_PATH_LOG_PARSING);
    }

    private void sendServerLogLineMessage(Server thnOlgServer, String line) {
        logger.debug("New line from {}: {}", thnOlgServer, line);
        final ServerLogLine serverLogLine = new ServerLogLine(thnOlgServer, line);
        jmsTemplate.send(session -> session.createObjectMessage(serverLogLine));
    }
}
