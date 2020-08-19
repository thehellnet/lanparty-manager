package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.utility.ServerBinaryRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ServerRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    private final ServerRepository serverRepository;

    private final Map<Long, ServerBinaryRunner> serverRunners = new HashMap<>();

    @Autowired
    public ServerRunner(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    protected void startRunner() {
        logger.info("START");

        List<Server> servers = serverRepository.findAllServerBinaryEnabled();
        for (Server server : servers) {
            ServerBinaryRunner serverBinaryRunner = new ServerBinaryRunner(server);
            serverBinaryRunner.start();
            serverRunners.put(server.getId(), serverBinaryRunner);
        }
    }

    @Override
    protected void stopRunner() {
        logger.info("STOP");
    }
}
