package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.logline.line.JoinLogLine;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatchPlayer;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.ServerMatchPlayerRepository;

@Service
public class ServerMatchPlayerService extends AbstractService {

    private final ServerMatchPlayerRepository serverMatchPlayerRepository;

    @Autowired
    protected ServerMatchPlayerService(SeatRepository seatRepository,
                                       PlayerRepository playerRepository,
                                       AppUserRepository appUserRepository,
                                       ServerMatchPlayerRepository serverMatchPlayerRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.serverMatchPlayerRepository = serverMatchPlayerRepository;
    }

    @Transactional
    public ServerMatchPlayer ensureServerMatchPlayerExists(ServerMatch serverMatch, JoinLogLine joinLogLine) {
        ServerMatchPlayer serverMatchPlayer = serverMatchPlayerRepository.findByGuidAndNum(joinLogLine.getGuid(), joinLogLine.getNum());
        if (serverMatchPlayer == null) {
            serverMatchPlayer = new ServerMatchPlayer(serverMatch, joinLogLine.getGuid(), joinLogLine.getNum());
            serverMatchPlayer.setJoinTs(joinLogLine.getDateTime());
            serverMatchPlayer.setServerMatch(serverMatch);
            serverMatchPlayer = serverMatchPlayerRepository.save(serverMatchPlayer);
        }
        return serverMatchPlayer;
    }
}
