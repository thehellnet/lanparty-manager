package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.Spectator;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.repository.SpectatorRepository;
import org.thehellnet.lanparty.manager.runner.SpectatorRunner;

import java.util.List;

@Service
@Transactional
public class SpectatorService {

    private final TaskExecutor taskExecutor;

    private final SpectatorRepository spectatorRepository;
    private final ServerRepository serverRepository;

    private final SpectatorRunner spectatorRunner;

    @Autowired
    public SpectatorService(TaskExecutor taskExecutor,
                            SpectatorRepository spectatorRepository,
                            ServerRepository serverRepository,
                            SpectatorRunner spectatorRunner) {
        this.taskExecutor = taskExecutor;
        this.spectatorRepository = spectatorRepository;
        this.serverRepository = serverRepository;
        this.spectatorRunner = spectatorRunner;
    }

    public List<Spectator> getAllEnabled() {
        return spectatorRepository.findAllByActiveIsTrue();
    }

    @Transactional(readOnly = true)
    public void joinSpectator(Server server) {
        server = serverRepository.findById(server.getId()).orElseThrow();

        List<Spectator> spectatorList = spectatorRepository.findAllByServer(server);
        for (Spectator spectator : spectatorList) {
            taskExecutor.execute(() -> joinAndSetReady(spectator));
        }
    }

    private void joinAndSetReady(Spectator spectator) {
        try {
            Thread.sleep(spectator.getTimeoutJoinSpectate());
            spectatorRunner.joinSpectate(spectator.getId());
            Thread.sleep(spectator.getTimeoutSetReady());
            spectatorRunner.setReady(spectator.getId());
        } catch (InterruptedException ignored) {
        }
    }
}
