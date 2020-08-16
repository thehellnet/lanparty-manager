package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Spectator;
import org.thehellnet.lanparty.manager.repository.SpectatorRepository;

import java.util.List;

@Service
@Transactional
public class SpectatorService {

    private final SpectatorRepository spectatorRepository;

    @Autowired
    public SpectatorService(SpectatorRepository spectatorRepository) {
        this.spectatorRepository = spectatorRepository;
    }

    public List<Spectator> getAllEnabled() {
        return spectatorRepository.findAllByActiveIsTrue();
    }
}
