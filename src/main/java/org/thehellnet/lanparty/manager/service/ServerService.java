package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.ServerRepository;

import java.util.List;

@Service
public class ServerService extends AbstractService {

    private final ServerRepository serverRepository;

    @Autowired
    protected ServerService(SeatRepository seatRepository,
                            PlayerRepository playerRepository,
                            AppUserRepository appUserRepository,
                            ServerRepository serverRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.serverRepository = serverRepository;
    }

    @Transactional(readOnly = true)
    public List<Server> getLogEnabledServers() {
        return serverRepository.findByLogParsingEnabledIsTrue();
    }
}
