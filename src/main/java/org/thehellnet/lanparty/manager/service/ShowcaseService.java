package org.thehellnet.lanparty.manager.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;

@Service
public class ShowcaseService extends AbstractService {

    private final ShowcaseRepository showcaseRepository;

    public ShowcaseService(ShowcaseRepository showcaseRepository) {
        this.showcaseRepository = showcaseRepository;
    }

    @Transactional(readOnly = true)
    public JSONObject prepareShowcase(String showcaseTag) {
        Showcase showcase = showcaseRepository.findByTag(showcaseTag);
        if (showcase == null) {
            throw new NotFoundException("Showcase not found");
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", showcase.getName());
        jsonObject.put("mode", showcase.getMode());
        jsonObject.put("tournamentName", showcase.getTournament().getName());
        jsonObject.put("tournamentGame", showcase.getTournament().getGame().getName());

        return jsonObject;
    }
}
