package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatchPlayer;
import org.thehellnet.lanparty.manager.repository.*;

import java.util.List;

@Service
public class ServerMatchService extends AbstractService {

    private final ServerMatchRepository serverMatchRepository;
    private final ServerMatchPlayerRepository serverMatchPlayerRepository;

    protected ServerMatchService(SeatRepository seatRepository,
                                 PlayerRepository playerRepository,
                                 AppUserRepository appUserRepository,
                                 ServerMatchRepository serverMatchRepository,
                                 ServerMatchPlayerRepository serverMatchPlayerRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.serverMatchRepository = serverMatchRepository;
        this.serverMatchPlayerRepository = serverMatchPlayerRepository;
    }

    @Transactional
    public void close(ServerMatch serverMatch) {
        serverMatch = serverMatchRepository.findById(serverMatch.getId()).orElseThrow();

        serverMatch.close();

        List<ServerMatchPlayer> serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch);

        for (ServerMatchPlayer serverMatchPlayer : serverMatchPlayers) {
            if (serverMatchPlayer.getJoinTs() == null) {
                serverMatchPlayer.setJoinTs(serverMatch.getStartTs());
            }
            if (serverMatchPlayer.getQuitTs() == null) {
                serverMatchPlayer.setQuitTs(serverMatch.getEndTs());
            }
        }

        serverMatchPlayerRepository.saveAll(serverMatchPlayers);
    }
}
