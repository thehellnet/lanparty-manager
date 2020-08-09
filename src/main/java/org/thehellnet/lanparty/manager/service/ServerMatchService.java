package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.*;
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

        Tournament tournament = null;

        Match match = serverMatch.getMatch();
        if (match != null) {
            tournament = match.getTournament();
        }

        List<ServerMatchPlayer> serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch);
        for (ServerMatchPlayer serverMatchPlayer : serverMatchPlayers) {
            fixPlayerTimestamps(serverMatch, serverMatchPlayer);
            setPlayerFromGuid(serverMatchPlayer, tournament);
        }

        serverMatchPlayerRepository.saveAll(serverMatchPlayers);
    }

    private void fixPlayerTimestamps(ServerMatch serverMatch, ServerMatchPlayer serverMatchPlayer) {
        if (serverMatchPlayer.getJoinTs() == null) {
            serverMatchPlayer.setJoinTs(serverMatch.getStartTs());
        }

        if (serverMatchPlayer.getQuitTs() == null) {
            serverMatchPlayer.setQuitTs(serverMatch.getEndTs());
        }
    }

    private void setPlayerFromGuid(ServerMatchPlayer serverMatchPlayer, Tournament tournament) {
        if (tournament == null) {
            return;
        }

        String guid = serverMatchPlayer.getGuid();

        Seat seat = seatRepository.findByGuidAndTournament(guid, tournament);
        if (seat == null) {
            return;
        }

        Player player = seat.getPlayer();
        serverMatchPlayer.setPlayer(player);
    }
}
