package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.constant.SettingConstant;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.service.SettingService;
import org.thehellnet.lanparty.manager.utility.ServerBinaryRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Transactional
public class ServerRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    private final SettingService settingService;

    private final ServerRepository serverRepository;

    private final Map<Long, ServerBinaryRunner> serverRunners = new HashMap<>();

    @Autowired
    public ServerRunner(SettingService settingService,
                        ServerRepository serverRepository) {
        this.settingService = settingService;
        this.serverRepository = serverRepository;
    }

    @Override
    protected boolean autostart() {
        return settingService.getBoolean(SettingConstant.AUTOSTART_SERVER_RUNNER);
    }

    @Override
    protected void startRunner() {
        logger.info("START");

        startAll();
    }

    @Override
    protected void stopRunner() {
        logger.info("STOP");

        stopAll();
    }

    @Transactional(readOnly = true)
    public void startAll() {
        synchronized (serverRunners) {
            List<Server> servers = serverRepository.findAllServerBinaryEnabled();
            for (Server server : servers) {
                startSingleNoSync(server);
            }
        }
    }

    @Transactional(readOnly = true)
    public void stopAll() {
        synchronized (serverRunners) {
            Set<Long> serverIds = serverRunners.keySet();
            List<Server> servers = serverRepository.findAllById(serverIds);
            for (Server server : servers) {
                stopSingleNoSync(server);
            }
        }
    }

    public void restartAll() {
        stopAll();
        startAll();
    }

    @Transactional(readOnly = true)
    public void startSingle(Long serverId) {
        synchronized (serverRunners) {
            Server server = serverRepository.findById(serverId).orElseThrow();
            startSingleNoSync(server);
        }
    }

    @Transactional(readOnly = true)
    public void stopSingle(Long serverId) {
        synchronized (serverRunners) {
            Server server = serverRepository.findById(serverId).orElseThrow();
            stopSingleNoSync(server);
        }
    }

    public void restartSingle(Long serverId) {
        stopSingle(serverId);
        startSingle(serverId);
    }

    private void startSingleNoSync(Server server) {
        ServerBinaryRunner serverBinaryRunner = new ServerBinaryRunner(server);
        serverBinaryRunner.start();
        serverRunners.put(server.getId(), serverBinaryRunner);
    }

    private void stopSingleNoSync(Server server) {
        ServerBinaryRunner serverBinaryRunner = serverRunners.get(server.getId());
        serverBinaryRunner.stop();
        serverBinaryRunner.join();
        serverRunners.remove(server.getId());
    }
}
