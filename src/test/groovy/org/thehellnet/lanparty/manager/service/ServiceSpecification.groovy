package org.thehellnet.lanparty.manager.service

import org.joda.time.DateTime
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.model.persistence.*

abstract class ServiceSpecification extends ContextSpecification {

    protected ServerMatch generateServerMatch(boolean closed = false, DateTime startTs = DateTime.now()) {
        Server server = createServer()
        Game game = gameRepository.findByTag(GAME_TAG)
        Gametype gametype = gametypeRepository.findByGameAndTag(game, GAMETYPE_TAG)
        GameMap gameMap = gameMapRepository.findByTagAndGame(GAMEMAP_TAG, game)

        ServerMatch serverMatch = new ServerMatch(server, gametype, gameMap)
        serverMatch.startTs = startTs
        if (closed) {
            serverMatch.close()
        }

        return serverMatchRepository.save(serverMatch)
    }
}
