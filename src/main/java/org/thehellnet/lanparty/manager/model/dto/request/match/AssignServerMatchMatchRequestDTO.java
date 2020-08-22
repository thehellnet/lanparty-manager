package org.thehellnet.lanparty.manager.model.dto.request.match;

public class AssignServerMatchMatchRequestDTO extends MatchRequestDTO {

    protected Long serverMatchId;

    public Long getServerMatchId() {
        return serverMatchId;
    }

    public void setServerMatchId(Long serverMatchId) {
        this.serverMatchId = serverMatchId;
    }
}
