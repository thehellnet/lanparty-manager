package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.constant.SettingConstant;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.Spectator;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommand;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommandAction;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.repository.SpectatorRepository;
import org.thehellnet.lanparty.manager.service.SettingService;
import org.thehellnet.lanparty.manager.service.SpectatorService;
import org.thehellnet.lanparty.manager.utility.spectator.SpectatorClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SpectatorRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpectatorRunner.class);

    private final TaskExecutor taskExecutor;

    private final ServerRepository serverRepository;
    private final SpectatorRepository spectatorRepository;

    private final SettingService settingService;
    private final SpectatorService spectatorService;

    private final Map<Long, SpectatorClient> spectatorClients = new HashMap<>();

    public SpectatorRunner(TaskExecutor taskExecutor,
                           ServerRepository serverRepository,
                           SpectatorRepository spectatorRepository,
                           SettingService settingService, SpectatorService spectatorService) {
        this.taskExecutor = taskExecutor;
        this.serverRepository = serverRepository;
        this.spectatorRepository = spectatorRepository;
        this.settingService = settingService;
        this.spectatorService = spectatorService;
    }

    @Transactional(readOnly = true)
    public void joinSpectator(Server server) {
        server = serverRepository.findById(server.getId()).orElseThrow();

        List<Spectator> spectatorList = spectatorRepository.findAllByServer(server);
        for (Spectator spectator : spectatorList) {
            taskExecutor.execute(() -> joinAndSetReady(spectator));
        }
    }

    @Override
    protected boolean autostart() {
        return settingService.getBoolean(SettingConstant.AUTOSTART_SPECTATOR_RUNNER);
    }

    @Override
    protected void startRunner() {
        logger.info("START");

        List<Spectator> spectatorList = spectatorService.getAllEnabled();

        for (Spectator spectator : spectatorList) {
            logger.debug("Starting SpectatorClient for spectator \"{}\"", spectator);

            SpectatorClient spectatorClient = new SpectatorClient(spectator.getAddress(), spectator.getPort());
            spectatorClient.start();

            logger.debug("Adding SpectatorClient for spectator {}", spectator);
            spectatorClients.put(spectator.getId(), spectatorClient);
        }
    }

    @Override
    protected void stopRunner() {
        logger.info("STOP");

        Set<Long> spectators = spectatorClients.keySet();
        Long[] keys = spectators.toArray(new Long[0]);

        for (Long key : keys) {
            SpectatorClient spectatorClient = spectatorClients.get(key);
            spectatorClient.stop();
            spectatorClient.join();

            logger.debug("Removing SpectatorClient for spectator {}", spectatorClient);
            spectatorClients.remove(key);
        }
    }

    private void joinAndSetReady(Spectator spectator) {
        long timeoutJoinSpectate = spectator.getTimeoutJoinSpectate() * 1000L;
        long timeoutSetReady = spectator.getTimeoutSetReady() * 1000L;

        try {
            Thread.sleep(timeoutJoinSpectate);
            joinSpectate(spectator.getId());
            Thread.sleep(timeoutSetReady);
            setReady(spectator.getId());
        } catch (InterruptedException ignored) { //NOSONAR
        }
    }

    private void joinSpectate(Long id) {
        SpectatorCommand command = new SpectatorCommand(SpectatorCommandAction.JOIN_SPECTATE);

        if (!spectatorClients.containsKey(id)) {
            return;
        }

        SpectatorClient spectatorClient = spectatorClients.get(id);
        spectatorClient.sendCommand(command);
    }

    private void setReady(Long id) {
        SpectatorCommand command = new SpectatorCommand(SpectatorCommandAction.SET_READY);

        if (!spectatorClients.containsKey(id)) {
            return;
        }

        SpectatorClient spectatorClient = spectatorClients.get(id);
        spectatorClient.sendCommand(command);
    }
}
