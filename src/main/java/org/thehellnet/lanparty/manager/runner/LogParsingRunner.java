package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.constant.JmsConstant;
import org.thehellnet.lanparty.manager.constant.SettingConstant;
import org.thehellnet.lanparty.manager.model.message.jms.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.service.ServerService;
import org.thehellnet.lanparty.manager.service.SettingService;
import org.thehellnet.lanparty.manager.utility.JmsUtility;
import org.thehellnet.utility.log.LogTailer;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LogParsingRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingRunner.class);

    private final ConnectionFactory connectionFactory;

    private final SettingService settingService;
    private final ServerService serverService;

    private final Map<Server, LogTailer> logTailers = new HashMap<>();
    private final Map<Server, String> lastLines = new HashMap<>();

    private final JmsTemplate jmsTemplate;

    @Autowired
    public LogParsingRunner(ConnectionFactory connectionFactory,
                            SettingService settingService,
                            ServerService serverService) {
        this.connectionFactory = connectionFactory;
        this.settingService = settingService;
        this.serverService = serverService;

        this.jmsTemplate = JmsUtility.prepareJmsTemplate(this.connectionFactory, JmsConstant.LOG_PARSING);
    }

    @Override
    protected boolean autostart() {
        return settingService.getBoolean(SettingConstant.AUTOSTART_LOG_PARSING);
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

    private void sendServerLogLineMessage(Server server, String line) {
        logger.debug("New line from {}: {}", server, line);

        if (lastLines.containsKey(server)) {
            String lastLine = lastLines.get(server);
            if (lastLine.equals(line)) {
                logger.debug("Duplicated log line for server {}", server);
                return;
            }
        }

        lastLines.put(server, line);
        final ServerLogLine serverLogLine = new ServerLogLine(server, line);
        jmsTemplate.send(session -> session.createObjectMessage(serverLogLine));
    }
}
