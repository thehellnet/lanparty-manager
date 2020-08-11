package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.model.constant.TournamentMode
import org.thehellnet.lanparty.manager.model.persistence.Team
import org.thehellnet.lanparty.manager.model.persistence.Tournament

class TournamentServiceTest extends ServiceSpecification {

    @Autowired
    private TournamentService tournamentService

    def "test generateMatchesTeamsDoubleRoundRobinElimination"() {
        given:
        Tournament tournament = generateTournament()

        Team alphaTeam = generateTeam("Alpha", tournament)
        Team bravoTeam = generateTeam("Bravo", tournament)
        Team charlieTeam = generateTeam("Charlie", tournament)
        Team deltaTeam = generateTeam("Delta", tournament)

        tournament.teams.add(alphaTeam)
        tournament.teams.add(bravoTeam)
        tournament.teams.add(charlieTeam)
        tournament.teams.add(deltaTeam)
        tournament = tournamentRepository.save(tournament)

        when:
        tournamentService.generateMatchesTeamsDoubleRoundRobinElimination(tournament)

        tournament = tournamentRepository.findById(tournament.id).orElseThrow()

        then:
        noExceptionThrown()

        tournament.matches.size() == 16
        tournament.matches.groupBy { it.level }.size() == 4
        tournament.matches.groupBy { it.localTeam == alphaTeam || it.guestTeam == alphaTeam }.get(true).size() == 6
        tournament.matches.groupBy { it.localTeam == bravoTeam || it.guestTeam == bravoTeam }.get(true).size() == 6
        tournament.matches.groupBy { it.localTeam == charlieTeam || it.guestTeam == charlieTeam }.get(true).size() == 6
        tournament.matches.groupBy { it.localTeam == deltaTeam || it.guestTeam == deltaTeam }.get(true).size() == 6
    }

    private Tournament generateTournament() {
        Tournament tournament = createTournament()
        tournament.mode = TournamentMode.TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION
        return tournamentRepository.save(tournament)
    }

    private Team generateTeam(String name, Tournament tournament) {
        Team team = new Team(name, tournament)
        return teamRepository.save(team)
    }
}
