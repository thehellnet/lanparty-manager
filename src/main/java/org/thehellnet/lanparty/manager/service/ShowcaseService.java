package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ShowcaseService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseService.class);

    private final ShowcaseRepository showcaseRepository;

    private Map<Showcase, WebSocketSession> sessions = new HashMap<>();

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
    public Showcase updateInfos(String ipAddress, Showcase showcase) {
        showcase = showcaseRepository.getOne(showcase.getId());
        showcase.setLastAddress(ipAddress);
        showcase.updateLastContact();
        showcase = showcaseRepository.save(showcase);
        return showcase;
    }

    public void addShowcase(String tag, WebSocketSession webSocketSession) {
        String ipAddress = Objects.requireNonNull(webSocketSession.getRemoteAddress()).toString();
        Showcase showcase = findByTag(tag);
        showcase = updateInfos(ipAddress, showcase);
        sessions.put(showcase, webSocketSession);

        logger.info("Showcase {} added from {}", tag, ipAddress);
    }

    public void removeShowcase(String tag) {
        Showcase showcase = findByTag(tag);
        if (!sessions.containsKey(showcase)) {
            return;
        }
        sessions.remove(showcase);

        logger.info("Showcase {} removed", tag);
    }
}
