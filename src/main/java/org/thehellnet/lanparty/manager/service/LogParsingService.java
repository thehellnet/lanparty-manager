package org.thehellnet.lanparty.manager.service;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.LanPartyException;
import org.thehellnet.lanparty.manager.model.logline.line.*;
import org.thehellnet.lanparty.manager.model.message.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.GameMapRepository;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;
import org.thehellnet.lanparty.manager.repository.ServerMatchRepository;
import org.thehellnet.lanparty.manager.repository.ServerRepository;
import org.thehellnet.lanparty.manager.settings.JmsSettings;
import org.thehellnet.lanparty.manager.utility.logline.LogLineParser;
import org.thehellnet.lanparty.manager.utility.logline.LogLineParserFactory;

import java.util.List;

@Service
@Transactional
public class LogParsingService {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingService.class);

    private final ServerRepository serverRepository;
    private final ServerMatchRepository serverMatchRepository;
    private final GametypeRepository gametypeRepository;
    private final GameMapRepository gameMapRepository;

    @Autowired
    public LogParsingService(ServerRepository serverRepository,
                             ServerMatchRepository serverMatchRepository,
                             GametypeRepository gametypeRepository,
                             GameMapRepository gameMapRepository) {
        this.serverRepository = serverRepository;
        this.serverMatchRepository = serverMatchRepository;
        this.gametypeRepository = gametypeRepository;
        this.gameMapRepository = gameMapRepository;
    }

    @JmsListener(destination = JmsSettings.JMS_PATH_LOG_PARSING)
    public void parseLogLine(final Message<ServerLogLine> message) {
        ServerLogLine serverLogLine = message.getPayload();

        String rawLogLine = serverLogLine.getLine();

        Server server = serverRepository.findById(serverLogLine.getThnOlgServer().getId()).orElseThrow();
        Game game = server.getGame();

        DateTime dateTime = DateTime.now();

        LogLineParser logLineParser = LogLineParserFactory.getLogLineParser(game, rawLogLine);
        LogLine logLine;

        try {
            logLine = logLineParser.parse(dateTime);
        } catch (LanPartyException e) {
            logger.warn("Unable to parse log line: {}", rawLogLine, e);
            return;
        }

        logger.debug("New server logLine from {}, {}", server, logLine);

        parseLogLineEvent(server, logLine);
    }

    @Transactional
    public void closeRunningServerMatch(Server server) {
        logger.info("Closing all running server matches");
        List<ServerMatch> runningServerMatches = serverMatchRepository.findAllRunningByServer(server);
        for (ServerMatch serverMatch : runningServerMatches) {
            serverMatch.close();
        }
        serverMatchRepository.saveAll(runningServerMatches);
    }

    @Transactional
    public ServerMatch createNewServerMatch(Server server, InitGameLogLine initGameLogLine) {
        String gametypeTag = initGameLogLine.getGametypeTag();
        String mapTag = initGameLogLine.getMapTag();

        Game game = server.getGame();
        Gametype gametype = gametypeRepository.findByGameAndTag(game, gametypeTag);
        GameMap gameMap = gameMapRepository.findByTagAndGame(mapTag, game);

        ServerMatch serverMatch = new ServerMatch(server, gametype, gameMap);
        serverMatch.setStartTs(initGameLogLine.getDateTime());
        serverMatch.setServer(server);

        serverMatch = serverMatchRepository.save(serverMatch);
        return serverMatch;
    }

    private void parseLogLineEvent(Server server, LogLine logLine) {
        if (logLine instanceof InitGameLogLine) {
            InitGameLogLine initGameLogLine = (InitGameLogLine) logLine;
            parseInitGameLogLine(server, initGameLogLine);
        } else if (logLine instanceof ShutdownGameLogLine) {
            ShutdownGameLogLine shutdownGameLogLine = (ShutdownGameLogLine) logLine;
            parseShutdownGameLogLine(server, shutdownGameLogLine);
        } else if (logLine instanceof JoinLogLine) {
            JoinLogLine joinLogLine = (JoinLogLine) logLine;
            parseJoinLogLine(server, joinLogLine);
        } else if (logLine instanceof QuitLogLine) {
            QuitLogLine quitLogLine = (QuitLogLine) logLine;
            parseQuitLogLine(server, quitLogLine);
        } else if (logLine instanceof DamageLogLine) {
            DamageLogLine damageLogLine = (DamageLogLine) logLine;
            parseDamageLogLine(server, damageLogLine);
        } else if (logLine instanceof KillLogLine) {
            KillLogLine killLogLine = (KillLogLine) logLine;
            parseKillLogLine(server, killLogLine);
        } else if (logLine instanceof SayLogLine) {
            SayLogLine sayLogLine = (SayLogLine) logLine;
            parseSayLogLine(server, sayLogLine);
        } else if (logLine instanceof WeaponLogLine) {
            WeaponLogLine weaponLogLine = (WeaponLogLine) logLine;
            parseWeaponLogLine(server, weaponLogLine);
        } else {
            logger.warn("Can't parse logline {}, parser not implemented", logLine);
        }
    }

    private void parseInitGameLogLine(Server server, InitGameLogLine initGameLogLine) {
        closeRunningServerMatch(server);
        ServerMatch serverMatch = createNewServerMatch(server, initGameLogLine);
        logger.debug("New ServerMatch created at {}: {}", initGameLogLine.getUptime(), serverMatch);
    }

    private void parseShutdownGameLogLine(Server server, ShutdownGameLogLine shutdownGameLogLine) {
        ServerMatch serverMatch = serverMatchRepository.findFirstByServerAndEndTsNullOrderByStartTsDesc(server);
        if (serverMatch != null) {
            serverMatch.close();
            serverMatch = serverMatchRepository.save(serverMatch);
        }
        logger.debug("ServerMatch finished at {}: {}", shutdownGameLogLine.getUptime(), serverMatch);
    }

    private void parseJoinLogLine(Server server, JoinLogLine joinLogLine) {
        throw new UnsupportedOperationException();
    }

    private void parseQuitLogLine(Server server, QuitLogLine quitLogLine) {
        throw new UnsupportedOperationException();
    }

    private void parseDamageLogLine(Server server, DamageLogLine damageLogLine) {
        throw new UnsupportedOperationException();
    }

    private void parseKillLogLine(Server server, KillLogLine killLogLine) {
        throw new UnsupportedOperationException();
    }

    private void parseSayLogLine(Server server, SayLogLine sayLogLine) {
        throw new UnsupportedOperationException();
    }

    private void parseWeaponLogLine(Server server, WeaponLogLine weaponLogLine) {
        throw new UnsupportedOperationException();
    }
}
