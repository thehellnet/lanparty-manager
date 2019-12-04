package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ShowcaseService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseService.class);

    private final Object SYNC = new Object();

    private final ShowcaseRepository showcaseRepository;

    private Map<Long, WebSocketSession> sessions = new HashMap<>();

    public ShowcaseService(ShowcaseRepository showcaseRepository) {
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

    @Scheduled(fixedDelay = 2000)
    public void sendToAllShowcases() {
        synchronized (SYNC) {
            sessions.forEach((showcaseId, webSocketSession) -> {
                logger.info("Sending to {}", showcaseId);

                TextMessage message = new TextMessage("Hi");

                try {
                    webSocketSession.sendMessage(message);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            });
        }
    }
}
