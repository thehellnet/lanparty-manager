package org.thehellnet.lanparty.manager.service

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.model.logline.line.JoinLogLine
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch
import org.thehellnet.lanparty.manager.model.persistence.ServerMatchPlayer

class ServerMatchPlayerServiceTest extends ServiceSpecification {

    private static final String SERVER_MATCH_PLAYER_GUID = "testguid"
    private static final int SERVER_MATCH_PLAYER_NUM = 1
    private static final String SERVER_MATCH_PLAYER_NICK = "testnick"

    private static final String SERVER_MATCH_PLAYER_GUID2 = "test2guid"
    private static final int SERVER_MATCH_PLAYER_NUM2 = 2
    private static final String SERVER_MATCH_PLAYER_NICK2 = "test2nick"

    @Autowired
    private ServerMatchPlayerService serverMatchPlayerService

    def "persistServerMatchPlayer with not existing records"() {
        given:
        ServerMatch serverMatch = generateServerMatch()

        JoinLogLine joinLogLine = new JoinLogLine(DateTime.now(), 0)
        joinLogLine.guid = SERVER_MATCH_PLAYER_GUID
        joinLogLine.num = SERVER_MATCH_PLAYER_NUM
        joinLogLine.nick = SERVER_MATCH_PLAYER_NICK

        when:
        List<ServerMatchPlayer> serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.isEmpty()

        when:
        serverMatchPlayerService.ensureServerMatchPlayerExists(serverMatch, joinLogLine)

        serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.size() == 1

        when:
        ServerMatchPlayer serverMatchPlayer = serverMatchPlayers.get(0)

        then:
        serverMatchPlayer.serverMatch == serverMatch
        serverMatchPlayer.num == SERVER_MATCH_PLAYER_NUM
        serverMatchPlayer.guid == SERVER_MATCH_PLAYER_GUID
        serverMatchPlayer.joinTs != null
    }

    def "persistServerMatchPlayer with another record"() {
        given:
        ServerMatch serverMatch = generateServerMatch()

        ServerMatchPlayer serverMatchPlayer = new ServerMatchPlayer(serverMatch, SERVER_MATCH_PLAYER_GUID2, SERVER_MATCH_PLAYER_NUM2)
        serverMatchPlayerRepository.save(serverMatchPlayer)

        JoinLogLine joinLogLine = new JoinLogLine(DateTime.now(), 0)
        joinLogLine.guid = SERVER_MATCH_PLAYER_GUID
        joinLogLine.num = SERVER_MATCH_PLAYER_NUM
        joinLogLine.nick = SERVER_MATCH_PLAYER_NICK

        when:
        List<ServerMatchPlayer> serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.size() == 1

        when:
        serverMatchPlayerService.ensureServerMatchPlayerExists(serverMatch, joinLogLine)

        serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.size() == 2

        when:
        serverMatchPlayer = serverMatchPlayerRepository.findByGuidAndNum(SERVER_MATCH_PLAYER_GUID, SERVER_MATCH_PLAYER_NUM)

        then:
        serverMatchPlayer.serverMatch == serverMatch
        serverMatchPlayer.num == SERVER_MATCH_PLAYER_NUM
        serverMatchPlayer.guid == SERVER_MATCH_PLAYER_GUID
        serverMatchPlayer.joinTs != null
    }

    def "persistServerMatchPlayer with the same record"() {
        given:
        ServerMatch serverMatch = generateServerMatch()

        ServerMatchPlayer serverMatchPlayer = new ServerMatchPlayer(serverMatch, SERVER_MATCH_PLAYER_GUID, SERVER_MATCH_PLAYER_NUM)
        serverMatchPlayerRepository.save(serverMatchPlayer)

        JoinLogLine joinLogLine = new JoinLogLine(DateTime.now(), 0)
        joinLogLine.guid = SERVER_MATCH_PLAYER_GUID
        joinLogLine.num = SERVER_MATCH_PLAYER_NUM
        joinLogLine.nick = SERVER_MATCH_PLAYER_NICK

        when:
        List<ServerMatchPlayer> serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.size() == 1

        when:
        serverMatchPlayerService.ensureServerMatchPlayerExists(serverMatch, joinLogLine)

        serverMatchPlayers = serverMatchPlayerRepository.findAllByServerMatch(serverMatch)

        then:
        serverMatchPlayers.size() == 1

        when:
        serverMatchPlayer = serverMatchPlayerRepository.findByGuidAndNum(SERVER_MATCH_PLAYER_GUID, SERVER_MATCH_PLAYER_NUM)

        then:
        serverMatchPlayer.serverMatch == serverMatch
        serverMatchPlayer.num == SERVER_MATCH_PLAYER_NUM
        serverMatchPlayer.guid == SERVER_MATCH_PLAYER_GUID
    }
}
