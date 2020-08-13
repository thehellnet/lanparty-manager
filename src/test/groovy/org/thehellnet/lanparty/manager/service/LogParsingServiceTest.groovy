package org.thehellnet.lanparty.manager.service

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.thehellnet.lanparty.manager.model.logline.line.InitGameLogLine
import org.thehellnet.lanparty.manager.model.message.jms.ServerLogLine
import org.thehellnet.lanparty.manager.model.persistence.*

class LogParsingServiceTest extends ServiceSpecification {

    private static final String GAMETYPE_TAG = "war"
    private static final String GAMEMAP_TAG = "mp_backlot"
    private static final int LOG_LINE_UPTIME = 0;

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
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
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
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 1

        serverMatches.get(0).id == serverMatch.id
        runningServerMatches.get(0).id == serverMatch.id
        runningServerMatches.get(0).endTs == null

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
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
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 1
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch.id
        serverMatches.get(0).endTs != null
        serverMatches.get(0).endTs.isAfter(serverMatches.get(0).startTs)

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
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
        DateTime now = DateTime.now().minusSeconds(1)

        ServerMatch serverMatch1 = new ServerMatch(server, gametype, gameMap)
        serverMatch1.startTs = now.plusMillis(1)
        serverMatch1.close()
        serverMatch1 = serverMatchRepository.save(serverMatch1)

        ServerMatch serverMatch2 = new ServerMatch(server, gametype, gameMap)
        serverMatch2.startTs = now.plusMillis(2)
        serverMatch2 = serverMatchRepository.save(serverMatch2)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 1

        serverMatches.get(0).id == serverMatch2.id
        serverMatches.get(0).endTs == null

        serverMatches.get(1).id == serverMatch1.id
        serverMatches.get(1).endTs != null

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch2.id
        serverMatches.get(0).endTs != null

        serverMatches.get(1).id == serverMatch1.id
        serverMatches.get(1).endTs != null
    }

    def "closeRunningServerMatch with two closed matches"() {
        given:
        DateTime now = DateTime.now().minusSeconds(1)

        ServerMatch serverMatch1 = new ServerMatch(server, gametype, gameMap)
        serverMatch1.startTs = now.plusMillis(1)
        serverMatch1.close()
        serverMatch1 = serverMatchRepository.save(serverMatch1)

        ServerMatch serverMatch2 = new ServerMatch(server, gametype, gameMap)
        serverMatch2.startTs = now.plusMillis(2)
        serverMatch2.close()
        serverMatch2 = serverMatchRepository.save(serverMatch2)

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch2.id
        serverMatches.get(0).endTs != null

        serverMatches.get(1).id == serverMatch1.id
        serverMatches.get(1).endTs != null

        when:
        logParsingService.closeRunningServerMatch(server)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)
        runningServerMatches = serverMatchRepository.findAllRunningByServer(server)

        then:
        serverMatches.size() == 2
        runningServerMatches.size() == 0

        serverMatches.get(0).id == serverMatch2.id
        serverMatches.get(0).endTs != null

        serverMatches.get(1).id == serverMatch1.id
        serverMatches.get(1).endTs != null
    }

    def "createNewServerMatch"() {
        given:
        InitGameLogLine initGameLogLine = new InitGameLogLine(logLineStartTs, LOG_LINE_UPTIME)
        initGameLogLine.gametypeTag = GAMETYPE_TAG
        initGameLogLine.mapTag = GAMEMAP_TAG

        ServerMatch expected = new ServerMatch(server, gametype, gameMap)
        expected.startTs = logLineStartTs

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 0

        when:
        ServerMatch actual = logParsingService.createNewServerMatch(server, initGameLogLine)

        then:
        actual != null
        actual.id != null
        actual.startTs == expected.startTs

        when:
        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1
    }

    def "parseLogLine InitGame"() {
        given:
        String input = "  0:00 InitGame: \\_Admin\\[hnt]^5theory\\_Location\\Italy\\_Maps\\CoD4 Standard Maps" +
                "\\_Website\\https://www.thehellnet.org\\g_compassShowEnemies\\0\\g_gametype\\war" +
                "\\gamename\\Call of Duty 4\\mapname\\mp_backlot\\protocol\\6\\shortversion\\1.7" +
                "\\sv_allowAnonymous\\1\\sv_disableClientConsole\\0\\sv_floodprotect\\4" +
                "\\sv_hostname\\^1The HellNet.org^7\\sv_maxclients\\20\\sv_maxPing\\300\\sv_maxRate\\25000" +
                "\\sv_minPing\\0\\sv_privateClients\\0\\sv_punkbuster\\1\\sv_pure\\1\\sv_voice\\0\\ui_maxclients\\32"

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 0

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        ServerMatch serverMatch = serverMatches.get(0)

        then:
        serverMatches.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
    }

    def "parseLogLine InitGame with already running ServerMatch"() {
        given:
        String input = "  0:00 InitGame: \\_Admin\\[hnt]^5theory\\_Location\\Italy\\_Maps\\CoD4 Standard Maps" +
                "\\_Website\\https://www.thehellnet.org\\g_compassShowEnemies\\0\\g_gametype\\war" +
                "\\gamename\\Call of Duty 4\\mapname\\mp_backlot\\protocol\\6\\shortversion\\1.7" +
                "\\sv_allowAnonymous\\1\\sv_disableClientConsole\\0\\sv_floodprotect\\4" +
                "\\sv_hostname\\^1The HellNet.org^7\\sv_maxclients\\20\\sv_maxPing\\300\\sv_maxRate\\25000" +
                "\\sv_minPing\\0\\sv_privateClients\\0\\sv_punkbuster\\1\\sv_pure\\1\\sv_voice\\0\\ui_maxclients\\32"

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        generateServerMatch()

        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        ServerMatch serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs == null

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 2

        when:
        serverMatch = serverMatches.get(1)

        then:
        serverMatch.endTs != null

        when:
        serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs == null
    }

    def "parseLogLine ShutdownGame with no running ServerMatch"() {
        given:
        String input = "1654:38 ShutdownGame:"

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 0

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 0
    }

    def "parseLogLine InitGame with one running ServerMatch"() {
        given:
        String input = "1654:38 ShutdownGame:"

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        generateServerMatch()

        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        ServerMatch serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs == null

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        serverMatch = serverMatches.get(0)

        then:
        serverMatch.endTs != null
    }

    def "parseLogLine InitGame with one already closed ServerMatch"() {
        given:
        String input = "1654:38 ShutdownGame:"

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        generateServerMatch(true)

        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        ServerMatch serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs != null

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 1

        when:
        serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs != null
    }

    def "parseLogLine InitGame with two ServerMatches, one closed and one running"() {
        given:
        String input = "1654:38 ShutdownGame:"

        DateTime now = DateTime.now().minusSeconds(1)

        ServerLogLine serverLogLine = new ServerLogLine(server, input)
        Message<ServerLogLine> message = TestMessageBuilder.generateMessage(serverLogLine) as Message<ServerLogLine>

        when:
        generateServerMatch(true, now.plusMillis(1))
        generateServerMatch(false, now.plusMillis(2))

        List<ServerMatch> serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 2

        when:
        ServerMatch serverMatch = serverMatches.get(1)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs != null

        when:
        serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs == null

        when:
        logParsingService.parseLogLine(message)

        serverMatches = serverMatchRepository.findAllByServerOrderByStartTsDesc(server)

        then:
        serverMatches.size() == 2

        when:
        serverMatch = serverMatches.get(1)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs != null

        when:
        serverMatch = serverMatches.get(0)

        then:
        serverMatch.id != null
        serverMatch.gametype == gametype
        serverMatch.gameMap == gameMap
        serverMatch.endTs != null
    }
}
