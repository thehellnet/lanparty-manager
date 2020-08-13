package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.MatchRepository;
import org.thehellnet.lanparty.manager.repository.TeamRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.List;

import static org.thehellnet.lanparty.manager.model.constant.TournamentMode.TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION;

@Service
public class TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatchRepository matchRepository,
                             TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void generateMatchesTeamsDoubleRoundRobinElimination(Tournament tournament) {
        logger.info("Generating matches for tournament {}", tournament);

        tournament = tournamentRepository.findById(tournament.getId()).orElseThrow();

        if (tournament.getMode() != TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION) {
            throw new InvalidDataException("Tournament mode is not valid");
        }

        List<Team> teams = teamRepository.findAllByTournament(tournament);
        if (teams.size() != 4) {
            throw new InvalidDataException("Teams count for this tournament mode must be 4");
        }

        Team alphaTeam = teams.get(0);
        Team bravoTeam = teams.get(1);
        Team charlieTeam = teams.get(2);
        Team deltaTeam = teams.get(3);

        logger.debug("Team Alpha: {}", alphaTeam);
        logger.debug("Team Bravo: {}", bravoTeam);
        logger.debug("Team Charlie: {}", charlieTeam);
        logger.debug("Team Delta: {}", deltaTeam);

        ensureRoundRobinMatchExists(tournament, alphaTeam, bravoTeam, 0, 0);
        ensureRoundRobinMatchExists(tournament, charlieTeam, deltaTeam, 0, 1);
        ensureRoundRobinMatchExists(tournament, alphaTeam, charlieTeam, 0, 2);
        ensureRoundRobinMatchExists(tournament, bravoTeam, deltaTeam, 0, 3);
        ensureRoundRobinMatchExists(tournament, bravoTeam, charlieTeam, 0, 4);
        ensureRoundRobinMatchExists(tournament, deltaTeam, alphaTeam, 0, 5);

        ensureRoundRobinMatchExists(tournament, bravoTeam, alphaTeam, 1, 6);
        ensureRoundRobinMatchExists(tournament, deltaTeam, charlieTeam, 1, 7);
        ensureRoundRobinMatchExists(tournament, charlieTeam, bravoTeam, 1, 8);
        ensureRoundRobinMatchExists(tournament, alphaTeam, deltaTeam, 1, 9);
        ensureRoundRobinMatchExists(tournament, charlieTeam, alphaTeam, 1, 10);
        ensureRoundRobinMatchExists(tournament, deltaTeam, bravoTeam, 1, 11);

        ensureEliminationMatchExists(tournament, 2, 12, "Half-final 1");
        ensureEliminationMatchExists(tournament, 2, 13, "Half-final 2");

        ensureEliminationMatchExists(tournament, 3, 14, "Third and Fourth");
        ensureEliminationMatchExists(tournament, 3, 15, "Final");

        tournamentRepository.save(tournament);
    }

    private void ensureRoundRobinMatchExists(Tournament tournament, Team localTeam, Team guestTeam, int level, int playOrder) {
        Match match = matchRepository.findByTournamentAndLocalTeamAndGuestTeamAndLevel(tournament, localTeam, guestTeam, level);

        if (match == null) {
            match = new Match(tournament, localTeam, guestTeam, level);
            match.setName(String.format("%s vs %s", localTeam, guestTeam));
        }

        match.setPlayOrder(playOrder);
        match = matchRepository.save(match);

        tournament.getMatches().add(match);

        logger.debug("Round-Robin Match: {}", match);
    }

    private void ensureEliminationMatchExists(Tournament tournament, int level, int playOrder, String name) {
        Match match = matchRepository.findByTournamentAndLevel(tournament, level);
        if (match == null) {
            match = new Match(tournament, level);
        }
        match.setPlayOrder(playOrder);
        match.setName(name);
        match = matchRepository.save(match);

        tournament.getMatches().add(match);

        logger.debug("Elimination Match: {}", match);
    }
}
