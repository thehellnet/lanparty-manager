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
import org.thehellnet.lanparty.manager.model.message.jms.ServerLogLine;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.lanparty.manager.runner.SpectatorRunner;
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
    private final ServerMatchPlayerRepository serverMatchPlayerRepository;
    private final GametypeRepository gametypeRepository;
    private final GameMapRepository gameMapRepository;

    private final ServerMatchService serverMatchService;

    private final SpectatorRunner spectatorRunner;

    @Autowired
    public LogParsingService(ServerRepository serverRepository,
                             ServerMatchRepository serverMatchRepository,
                             ServerMatchPlayerRepository serverMatchPlayerRepository,
                             GametypeRepository gametypeRepository,
                             GameMapRepository gameMapRepository,
                             ServerMatchService serverMatchService,
                             SpectatorRunner spectatorRunner) {
        this.serverRepository = serverRepository;
        this.serverMatchRepository = serverMatchRepository;
        this.serverMatchPlayerRepository = serverMatchPlayerRepository;
        this.gametypeRepository = gametypeRepository;
        this.gameMapRepository = gameMapRepository;
        this.serverMatchService = serverMatchService;
        this.spectatorRunner = spectatorRunner;
    }

    @Transactional
    @JmsListener(destination = JmsSettings.LOG_PARSING)
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
            logger.warn("Unable to parse log line: {}", rawLogLine);
            return;
        }

        logger.debug("New server logLine from {}, {}", server, logLine);

        if (logLine instanceof InitGameLogLine) {
            InitGameLogLine initGameLogLine = (InitGameLogLine) logLine;
            parseInitGameLogLine(server, initGameLogLine);
            return;
        }

        ServerMatch serverMatch = serverMatchRepository.findFirstByServerAndEndTsNullOrderByStartTsDesc(server);
        if (serverMatch == null) {
            logger.warn("New LogLine received but no ServerMatch found for server {}", server);
            return;
        }

        if (logLine instanceof ShutdownGameLogLine) {
            ShutdownGameLogLine shutdownGameLogLine = (ShutdownGameLogLine) logLine;
            parseShutdownGameLogLine(serverMatch, server, shutdownGameLogLine);
        } else if (logLine instanceof JoinLogLine) {
            JoinLogLine joinLogLine = (JoinLogLine) logLine;
            parseJoinLogLine(serverMatch, server, joinLogLine);
        } else if (logLine instanceof QuitLogLine) {
            QuitLogLine quitLogLine = (QuitLogLine) logLine;
            parseQuitLogLine(serverMatch, server, quitLogLine);
        } else if (logLine instanceof DamageLogLine) {
            DamageLogLine damageLogLine = (DamageLogLine) logLine;
            parseDamageLogLine(serverMatch, server, damageLogLine);
        } else if (logLine instanceof KillLogLine) {
            KillLogLine killLogLine = (KillLogLine) logLine;
            parseKillLogLine(serverMatch, server, killLogLine);
        } else if (logLine instanceof SayLogLine) {
            SayLogLine sayLogLine = (SayLogLine) logLine;
            parseSayLogLine(serverMatch, server, sayLogLine);
        } else if (logLine instanceof WeaponLogLine) {
            WeaponLogLine weaponLogLine = (WeaponLogLine) logLine;
            parseWeaponLogLine(serverMatch, server, weaponLogLine);
        } else {
            logger.warn("Can't parse logline {}, parser not implemented", logLine);
        }
    }

    @Transactional
    public void parseInitGameLogLine(Server server, InitGameLogLine initGameLogLine) {
        closeRunningServerMatch(server);
        ServerMatch serverMatch = createNewServerMatch(server, initGameLogLine);
        spectatorRunner.joinSpectator(server);
        logger.debug("New ServerMatch created at {}: {}", initGameLogLine.getUptime(), serverMatch);
    }

    @Transactional
    public void parseShutdownGameLogLine(ServerMatch serverMatch, Server server, ShutdownGameLogLine shutdownGameLogLine) {
        serverMatchService.close(serverMatch);
        logger.debug("ServerMatch finished at {}: {}", shutdownGameLogLine.getUptime(), serverMatch);
    }

    @Transactional
    public void parseJoinLogLine(ServerMatch serverMatch, Server server, JoinLogLine joinLogLine) {
        String guid = joinLogLine.getGuid();
        int num = joinLogLine.getNum();

        ServerMatchPlayer serverMatchPlayer = serverMatchPlayerRepository.findByServerMatchAndGuidAndNum(serverMatch, guid, num);
        if (serverMatchPlayer == null) {
            serverMatchPlayer = new ServerMatchPlayer(serverMatch, guid, num);
            serverMatchPlayer.setJoinTs(joinLogLine.getDateTime());
        }

        serverMatchPlayer.setQuitTs(null);
        serverMatchPlayer = serverMatchPlayerRepository.save(serverMatchPlayer);

        logger.debug("ServerMatchPlayer {} joined at {}", serverMatchPlayer, serverMatchPlayer.getJoinTs());
    }

    @Transactional
    public void parseQuitLogLine(ServerMatch serverMatch, Server server, QuitLogLine quitLogLine) {
        String guid = quitLogLine.getGuid();
        int num = quitLogLine.getNum();

        ServerMatchPlayer serverMatchPlayer = serverMatchPlayerRepository.findByServerMatchAndGuidAndNum(serverMatch, guid, num);
        if (serverMatchPlayer == null) {
            logger.warn("Quit event received but no ServerMatchPlayer found on match {}", serverMatch);
            return;
        }

        serverMatchPlayer.setQuitTs(quitLogLine.getDateTime());
        serverMatchPlayer = serverMatchPlayerRepository.save(serverMatchPlayer);

        logger.debug("ServerMatchPlayer {} quit at {}", serverMatchPlayer, serverMatchPlayer.getQuitTs());
    }

    private void parseDamageLogLine(ServerMatch serverMatch, Server server, DamageLogLine damageLogLine) {
        logger.debug("In ServerMatch \"{}\" player {} damages {}", serverMatch, damageLogLine.getOffendingNum(), damageLogLine.getAffectedNum());
    }

    private void parseKillLogLine(ServerMatch serverMatch, Server server, KillLogLine killLogLine) {
        ServerMatchPlayer affectedPlayer = serverMatchPlayerRepository.findByServerMatchAndGuidAndNum(serverMatch, killLogLine.getAffectedGuid(), killLogLine.getAffectedNum());
        if (affectedPlayer == null) {
            logger.warn("Affected player NULL!");
            return;
        }

        ServerMatchPlayer offendingPlayer;

        boolean selfKill = killLogLine.getOffendingNum() == -1 && (killLogLine.getOffendingGuid() == null || (killLogLine.getOffendingGuid().equals(killLogLine.getAffectedGuid())));

        if (selfKill) {
            offendingPlayer = affectedPlayer;
            affectedPlayer.addDeath();
        } else {
            offendingPlayer = serverMatchPlayerRepository.findByServerMatchAndGuidAndNum(serverMatch, killLogLine.getOffendingGuid(), killLogLine.getOffendingNum());
            if (offendingPlayer == null) {
                logger.warn("Offending player NULL!");
                return;
            }

            affectedPlayer.addDeath();
            offendingPlayer.addKill();
        }

        serverMatchPlayerRepository.save(affectedPlayer);
        serverMatchPlayerRepository.save(offendingPlayer);

        logger.debug("In ServerMatch \"{}\" player {} kills {}{}", serverMatch, offendingPlayer, affectedPlayer, selfKill ? " - self-kill" : "");
    }

    private void parseSayLogLine(ServerMatch serverMatch, Server server, SayLogLine sayLogLine) {
        logger.debug("In ServerMatch \"{}\" player {} says \"{}\"", serverMatch, sayLogLine.getNum(), sayLogLine.getMessage());
    }

    private void parseWeaponLogLine(ServerMatch serverMatch, Server server, WeaponLogLine weaponLogLine) {
        logger.debug("In ServerMatch \"{}\" player {} drops {}", serverMatch, weaponLogLine.getNum(), weaponLogLine.getWeapon());
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
        logger.info("Creating new server match");

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
}
