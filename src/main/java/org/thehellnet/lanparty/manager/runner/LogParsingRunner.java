package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.service.ServerService;
import org.thehellnet.utility.log.LogTailer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LogParsingRunner extends AbstractRunner implements Runner {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingRunner.class);

    private final ServerService serverService;
    private final JmsTemplate jmsTemplate;

    private final Map<Server, LogTailer> logTailers = new HashMap<>();

    @Autowired
    public LogParsingRunner(ServerService serverService,
                            JmsTemplate jmsTemplate) {
        this.serverService = serverService;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    protected void startRunner() {
        logger.info("START");

        List<Server> serverList = serverService.getLogEnabledServers();

        for (Server thnOlgServer : serverList) {
            logger.debug("Starting new LogTailer for server {}", thnOlgServer);

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

    private void sendServerLogLineMessage(Server thnOlgServer, String line) {
        final ServerLogLine serverLogLine = new ServerLogLine(thnOlgServer, line);
        jmsTemplate.send(session -> session.createObjectMessage(serverLogLine));
    }
}
