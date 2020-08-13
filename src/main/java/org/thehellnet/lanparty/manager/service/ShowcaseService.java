package org.thehellnet.lanparty.manager.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Pane;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.model.protocol.*;
import org.thehellnet.lanparty.manager.repository.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class ShowcaseService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseService.class);

    private final Object SYNC = new Object();

    private final ShowcaseRepository showcaseRepository;
    private final PaneRepository paneRepository;

    private final Map<Long, WebSocketSession> sessions = new HashMap<>();

    @Autowired
    public ShowcaseService(SeatRepository seatRepository,
                           PlayerRepository playerRepository,
                           AppUserRepository appUserRepository,
                           ShowcaseRepository showcaseRepository,
                           PaneRepository paneRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.showcaseRepository = showcaseRepository;
        this.paneRepository = paneRepository;
    }

    @Transactional(readOnly = true)
    public Showcase findByTag(String tag) {
        Showcase showcase = showcaseRepository.findByTag(tag);
        if (showcase == null) {
            throw new NotFoundException();
        }
        return showcase;
    }

    @Transactional
    public void addShowcase(String tag, WebSocketSession webSocketSession) {
        synchronized (SYNC) {
            String ipAddress = Objects.requireNonNull(webSocketSession.getRemoteAddress()).getAddress().getHostAddress();

            Showcase showcase = showcaseRepository.findByTag(tag);
            if (showcase == null) {
                showcase = new Showcase(tag);
            }

            showcase.setConnected(true);
            showcase.setLastAddress(ipAddress);
            showcase.updateLastContact();
            showcase = showcaseRepository.save(showcase);

            sessions.put(showcase.getId(), webSocketSession);

            logger.info("Showcase {} added from {}", tag, ipAddress);
        }
    }

    @Transactional
    public void removeShowcase(String tag) {
        synchronized (SYNC) {
            Showcase showcase = findByTag(tag);
            showcase.setConnected(false);
            showcase = showcaseRepository.save(showcase);

            sessions.remove(showcase.getId());

            logger.info("Showcase {} removed", tag);
        }
    }

//    @Scheduled(fixedDelay = 2000)
//    public void sendToAllShowcases() {
//        synchronized (SYNC) {
//            sessions.forEach((showcaseId, webSocketSession) -> {
//                logger.info("Sending to {}", showcaseId);
//
//                TextMessage message = new TextMessage("Hi");
//
//                try {
//                    webSocketSession.sendMessage(message);
//                } catch (IOException e) {
//                    logger.error(e.getMessage());
//                }
//            });
//        }
//    }

    public void parseMessage(String tag, String message) {
        Showcase showcase = findByTag(tag);
        logger.debug("Message from {}: {}", showcase, message);

        CommandParser commandParser = new CommandParser(message);
        Command command = commandParser.parse();

        parseCommand(showcase, command);
    }

    private void parseCommand(Showcase showcase, Command command) {
        showcase = showcaseRepository.findById(showcase.getId()).orElseThrow();

        JSONObject args = new JSONObject();

        if (command.getNoun() == ShowcaseNoun.TEST) {
            if (command.getVerb() == ShowcaseVerb.PING) {
                handleTestPing(args);
            } else if (command.getVerb() == ShowcaseVerb.TEXT) {
                handleTestText(command, args);
            } else if (command.getVerb() == ShowcaseVerb.GET) {
            }

        } else if (command.getNoun() == ShowcaseNoun.PANE) {
            if (command.getVerb() == ShowcaseVerb.GET) {
                if (command.getArgs().isEmpty()) {
                    handlePaneGet(showcase, args);
                } else {
                    handlePaneGetId(showcase, command.getArgs(), args);
                }
            }
        }

        Command responseCommand = new Command(command, args);
        send(showcase, responseCommand);
    }

    private void send(Showcase showcase, Command responseCommand) {
        WebSocketSession webSocketSession = sessions.get(showcase.getId());
        CommandSerializer commandSerializer = new CommandSerializer(responseCommand);
        String message = commandSerializer.serialize();
        TextMessage textMessage = new TextMessage(message);

        try {
            webSocketSession.sendMessage(textMessage);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleTestPing(JSONObject args) {
        args.put("ping", "pong");
    }

    private void handleTestText(Command command, JSONObject args) {
        args.put("text", command.getArgs().getString("text"));
    }

    private void handlePaneGet(Showcase showcase, JSONObject args) {
        List<Pane> panes = paneRepository.findAllByShowcaseOrderByDisplayOrderDesc(showcase);
        JSONArray panesArray = new JSONArray();
        for (Pane pane : panes) {
            panesArray.put(pane.getId());
        }

        args.put("count", panes.size());
        args.put("panes", panesArray);
    }

    private void handlePaneGetId(Showcase showcase, JSONObject commandArgs, JSONObject args) {
        if (!commandArgs.has("id")) {
            throw new InvalidDataException();
        }

        Long paneId = commandArgs.getLong("id");
        Pane pane = paneRepository.findByShowcaseAndId(showcase, paneId);
        args.put("pane", pane);
    }
}
