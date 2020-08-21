package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.MatchParent;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.MatchParentRepository;
import org.thehellnet.lanparty.manager.repository.MatchRepository;
import org.thehellnet.lanparty.manager.repository.TeamRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.thehellnet.lanparty.manager.model.constant.TournamentMode.TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION;

@Service
public class TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final MatchParentRepository matchParentRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             MatchRepository matchRepository,
                             MatchParentRepository matchParentRepository,
                             TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
        this.matchParentRepository = matchParentRepository;
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

        createRoundRobinMatch(tournament, alphaTeam, bravoTeam, 0, 0);
        createRoundRobinMatch(tournament, charlieTeam, deltaTeam, 0, 1);
        createRoundRobinMatch(tournament, alphaTeam, charlieTeam, 0, 2);
        createRoundRobinMatch(tournament, bravoTeam, deltaTeam, 0, 3);
        createRoundRobinMatch(tournament, bravoTeam, charlieTeam, 0, 4);
        createRoundRobinMatch(tournament, deltaTeam, alphaTeam, 0, 5);

        createRoundRobinMatch(tournament, bravoTeam, alphaTeam, 1, 6);
        createRoundRobinMatch(tournament, deltaTeam, charlieTeam, 1, 7);
        createRoundRobinMatch(tournament, charlieTeam, bravoTeam, 1, 8);
        createRoundRobinMatch(tournament, alphaTeam, deltaTeam, 1, 9);
        createRoundRobinMatch(tournament, charlieTeam, alphaTeam, 1, 10);
        createRoundRobinMatch(tournament, deltaTeam, bravoTeam, 1, 11);

        Match match1 = createEliminationMatch(tournament, 2, 12, "Half-final 1", Collections.emptyList());
        Match match2 = createEliminationMatch(tournament, 2, 13, "Half-final 2", Collections.emptyList());

        MatchParent matchParent1 = createMatchParent(match1, 1);
        MatchParent matchParent2 = createMatchParent(match2, 2);

        List<MatchParent> matchParents = new ArrayList<>();
        matchParents.add(matchParent1);
        matchParents.add(matchParent2);

        createEliminationMatch(tournament, 3, 14, "Third and Fourth", Collections.emptyList());
        createEliminationMatch(tournament, 3, 15, "Final", matchParents);

        tournamentRepository.save(tournament);
    }

    private void createRoundRobinMatch(Tournament tournament, Team localTeam, Team guestTeam, int level, int playOrder) {
        Match match = new Match(tournament, localTeam, guestTeam, level);
        match.setName(String.format("%s vs %s", localTeam, guestTeam));
        match.setPlayOrder(playOrder);
        match = matchRepository.save(match);

        tournament.getMatches().add(match);

        logger.debug("Round-Robin Match: {}", match);
    }

    private Match createEliminationMatch(Tournament tournament, int level, int playOrder, String name, List<MatchParent> matchParents) {
        Match match = new Match(tournament, level);
        match.setPlayOrder(playOrder);
        match.setName(name);
        match.getMatchParents().addAll(matchParents);
        match = matchRepository.save(match);

        tournament.getMatches().add(match);

        logger.debug("Elimination Match: {}", match);

        return match;
    }

    private MatchParent createMatchParent(Match match, int order) {
        MatchParent matchParent = new MatchParent(match, order);
        matchParent = matchParentRepository.save(matchParent);

        logger.debug("Match Parent: {}", matchParent);

        return matchParent;
    }
}
