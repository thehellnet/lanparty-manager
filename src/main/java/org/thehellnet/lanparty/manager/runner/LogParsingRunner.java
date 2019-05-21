package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.utility.log.LogTailer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LogParsingRunner extends AbstractRunner implements Runner {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingRunner.class);

    private final Object SYNC = new Object();

    private final ServerRepository serverRepository;

    private Map<Server, LogTailer> logTailers = new HashMap<>();

    public LogParsingRunner(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public void start() {
        synchronized (SYNC) {
            startLogParsing();
        }
    }

    @Override
    public void stop() {
        synchronized (SYNC) {
            stopLogParsing();
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    protected void startRunner() {
        start();
    }

    @Override
    protected void stopRunner() {
        stop();
    }

    @Transactional(readOnly = true)
    protected List<Server> readServers() {
        return serverRepository.findByLogParsingEnabledIsTrue();
    }

    private void startLogParsing() {
        logger.info("START");

        List<Server> serverList = readServers();

        for (Server thnOlgServer : serverList) {
            logger.debug("Starting new LogTailer for server {}", thnOlgServer);

            if (thnOlgServer.getLogFile() == null
                    || thnOlgServer.getLogFile().length() == 0) {
                logger.warn("Empty logFile path for server {}", thnOlgServer);
                return;
            }

            File logFile = new File(thnOlgServer.getLogFile());
            LogTailer logTailer = new LogTailer(logFile, line -> runLogLineParser(thnOlgServer, line));
            logTailer.start();

            logger.debug("Adding LogTailer for server {}", thnOlgServer);
            logTailers.put(thnOlgServer, logTailer);
        }
    }

    private void runLogLineParser(Server thnOlgServer, String line) {
//        Thread thread = new Thread(() -> lineParsingService.parseLine(thnOlgServer, line));
//        thread.start();
    }

    private void stopLogParsing() {
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
}
