package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.server.WrongServerException;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch;
import org.thehellnet.lanparty.manager.repository.MatchRepository;
import org.thehellnet.lanparty.manager.repository.ServerMatchRepository;

@Service
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final ServerMatchRepository serverMatchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository,
                        ServerMatchRepository serverMatchRepository) {
        this.matchRepository = matchRepository;
        this.serverMatchRepository = serverMatchRepository;
    }

    @Transactional
    public void assignServerMatch(Long matchId, Long serverMatchId) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        ServerMatch serverMatch = serverMatchRepository.findById(serverMatchId).orElseThrow();

        if (!match.getServer().getId().equals(serverMatch.getServer().getId())) {
            throw new WrongServerException("Match and ServerMatch have different servers");
        }

        match.setServerMatch(serverMatch);
        matchRepository.save(match);
    }
}
