package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.model.persistence.Spectator;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommand;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommandAction;
import org.thehellnet.lanparty.manager.service.SpectatorService;
import org.thehellnet.lanparty.manager.utility.spectator.SpectatorClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SpectatorRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpectatorRunner.class);

    private final SpectatorService spectatorService;

    private final Map<Long, SpectatorClient> spectatorClients = new HashMap<>();

    public SpectatorRunner(SpectatorService spectatorService) {
        this.spectatorService = spectatorService;
    }

    public void joinSpectate(Long id) {
        SpectatorCommand command = new SpectatorCommand(SpectatorCommandAction.JOIN_SPECTATE);

        if (!spectatorClients.containsKey(id)) {
            return;
        }

        SpectatorClient spectatorClient = spectatorClients.get(id);
        spectatorClient.sendCommand(command);
    }

    public void setReady(Long id) {
        SpectatorCommand command = new SpectatorCommand(SpectatorCommandAction.SET_READY);

        if (!spectatorClients.containsKey(id)) {
            return;
        }

        SpectatorClient spectatorClient = spectatorClients.get(id);
        spectatorClient.sendCommand(command);
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
}
