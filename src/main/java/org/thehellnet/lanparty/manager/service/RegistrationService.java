package org.thehellnet.lanparty.manager.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.List;

@Service
@Transactional
public class RegistrationService extends AbstractService {

    private final TournamentRepository tournamentRepository;

    public RegistrationService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public JSONArray getTournaments() {
        List<Tournament> tournaments = tournamentRepository.findRegistrables();

        JSONArray jsonArray = new JSONArray();
        for (Tournament tournament : tournaments) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", tournament.getId());
            jsonObject.put("name", tournament.getName());
            jsonObject.put("game", tournament.getGame().getName());
            jsonObject.put("start", tournament.getStartDateTime());
            jsonObject.put("end", tournament.getEndDateTime());
            jsonObject.put("registration_start", tournament.getStartRegistrationDateTime());
            jsonObject.put("registration_end", tournament.getEndRegistrationDateTime());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
}
