package org.thehellnet.lanparty.manager.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.thehellnet.lanparty.manager.exception.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Pane;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.model.protocol.Command;
import org.thehellnet.lanparty.manager.model.protocol.CommandParser;
import org.thehellnet.lanparty.manager.model.protocol.ShowcaseNoun;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;

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

    private final Map<Long, WebSocketSession> sessions = new HashMap<>();

    @Autowired
    public ShowcaseService(SeatRepository seatRepository,
                           PlayerRepository playerRepository,
                           AppUserRepository appUserRepository,
                           ShowcaseRepository showcaseRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.showcaseRepository = showcaseRepository;
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

    public void parseCommand(Showcase showcase, Command command) {
        showcase = showcaseRepository.findById(showcase.getId()).orElseThrow();

        JSONObject response;

        if (command.getNoun() == ShowcaseNoun.TEST) {
            switch (command.getVerb()) {
                case PING:
                    response = new JSONObject();
                    response.put("ping", "pong");
                    send(showcase, response);
                    break;

                case TEXT:
                    response = new JSONObject();
                    response.put("text", command.getArgs().getString("text"));
                    send(showcase, response);
                    break;
            }

        } else if (command.getNoun() == ShowcaseNoun.PANE) {
            switch (command.getVerb()) {
                case GET:
                    List<Pane> panes = showcase.getPanes();
                    response = new JSONObject();
                    response.put("panes", panes);
                    send(showcase, response);
                    break;
            }
        }
    }

    private void send(Showcase showcase, JSONObject response) {
        WebSocketSession webSocketSession = sessions.get(showcase.getId());
        String message = response.toString();
        TextMessage textMessage = new TextMessage(message);

        try {
            webSocketSession.sendMessage(textMessage);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
