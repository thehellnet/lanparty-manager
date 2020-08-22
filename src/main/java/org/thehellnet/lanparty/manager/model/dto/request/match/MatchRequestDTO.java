package org.thehellnet.lanparty.manager.model.dto.request.match;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public abstract class MatchRequestDTO extends RequestDTO {

    protected Long matchId;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
}
