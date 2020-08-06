package org.thehellnet.lanparty.manager.service

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.model.logline.line.InitGameLogLine
import org.thehellnet.lanparty.manager.model.persistence.*
import org.thehellnet.lanparty.manager.repository.GameMapRepository
import org.thehellnet.lanparty.manager.repository.GametypeRepository
import org.thehellnet.lanparty.manager.repository.ServerMatchRepository

class LogParsingServiceTest extends ServiceSpecification {

    private static final String GAMETYPE_TAG = "war"
    private static final String GAMEMAP_TAG = "mp_carentan"
    private static final int LOG_LINE_UPTIME = 0;

    @Autowired
    private ServerMatchRepository serverMatchRepository

    @Autowired
    private GametypeRepository gametypeRepository

    @Autowired
    private GameMapRepository gameMapRepository

    @Autowired
    private LogParsingService logParsingService;

    private Server server
    private Game game
    private Gametype gametype
    private GameMap gameMap

    private DateTime logLineStartTs

    @SuppressWarnings("unused")
    def setup() {
        server = createServer()
        game = gameRepository.findByTag(GAME_TAG)
        gametype = gametypeRepository.findByGameAndTag(game, GAMETYPE_TAG)
        gameMap = gameMapRepository.findByTagAndGame(GAMEMAP_TAG, game)

        logLineStartTs = DateTime.now()
    }

    def "closeRunningServerMatch with no matches"() {
        when:
        logParsingService.closeRunningServerMatch(server)
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 0
        runningServerMatches.size() == 0
    }

    def "closeRunningServerMatch with one match opened"() {
        given:
        ServerMatch serverMatch = new ServerMatch(server, gametype, gameMap)
        serverMatch.startTs = DateTime.now().minusSeconds(1)
        serverMatch = serverMatchRepository.save(serverMatch)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 1

        serverMatches.get(0).id == serverMatch.id
        runningServerMatches.get(0).id == serverMatch.id
        runningServerMatches.get(0).endTs == null

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServer(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)
    }

    def "closeRunningServerMatch with one match already closed"() {
        given:
        ServerMatch serverMatch = new ServerMatch(server, gametype, gameMap)
        serverMatch.startTs = DateTime.now().minusSeconds(1)
        serverMatch.close()
        serverMatch = serverMatchRepository.save(serverMatch)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServer(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)
    }

    def "closeRunningServerMatch with two matches, one open one closed"() {
        given:
        ServerMatch serverMatch1 = new ServerMatch(server, gametype, gameMap)
        serverMatch1.startTs = DateTime.now().minusSeconds(1)
        serverMatch1.close()
        serverMatch1 = serverMatchRepository.save(serverMatch1)

        ServerMatch serverMatch2 = new ServerMatch(server, gametype, gameMap)
        serverMatch2.startTs = DateTime.now().minusSeconds(1)
        serverMatch2 = serverMatchRepository.save(serverMatch2)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 1

        serverMatches.get(0).id == serverMatch1.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        serverMatches.get(1).id == serverMatch2.id
        serverMatches.get(1).endTs == null

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServer(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch1.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        serverMatches.get(1).id == serverMatch2.id
        serverMatches.get(1).endTs != null
        serverMatches.get(1).endTs.isAfter(serverMatches.get(1).startTs)
    }

    def "closeRunningServerMatch with two closed matches"() {
        given:
        ServerMatch serverMatch1 = new ServerMatch(server, gametype, gameMap)
        serverMatch1.startTs = DateTime.now().minusSeconds(1)
        serverMatch1.close()
        serverMatch1 = serverMatchRepository.save(serverMatch1)

        ServerMatch serverMatch2 = new ServerMatch(server, gametype, gameMap)
        serverMatch2.startTs = DateTime.now().minusSeconds(1)
        serverMatch2.close()
        serverMatch2 = serverMatchRepository.save(serverMatch2)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch1.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        serverMatches.get(1).id == serverMatch2.id
        serverMatches.get(1).endTs != null
        serverMatches.get(1).endTs.isAfter(serverMatches.get(1).startTs)

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServer(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch1.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        serverMatches.get(1).id == serverMatch2.id
        serverMatches.get(1).endTs != null
        serverMatches.get(1).endTs.isAfter(serverMatches.get(1).startTs)
    }

    def "createNewServerMatch"() {
        given:
        InitGameLogLine initGameLogLine = new InitGameLogLine(logLineStartTs, LOG_LINE_UPTIME)
        initGameLogLine.gametypeTag = GAMETYPE_TAG
        initGameLogLine.mapTag = GAMEMAP_TAG

        ServerMatch expected = new ServerMatch(server, gametype, gameMap)
        expected.startTs = logLineStartTs

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServer(server)

        then:
        serverMatches.size() == 0

        when:
        ServerMatch actual = logParsingService.createNewServerMatch(server, initGameLogLine)

        then:
        actual != null
        actual.id != null
        actual.startTs == expected.startTs

        when:
        serverMatches = serverMatchRepository.findAllByServer(server)

        then:
        serverMatches.size() == 1
    }
}
