package org.thehellnet.lanparty.manager.model.dto.service;

public class MatchServiceDTO extends ServiceDTO {

    public String name;
    public Long tournamentId;
    public Long serverId;
    public Long gameMapId;
    public Long gametypeId;
    public Long localTeamId;
    public Long guestTeamId;

    public MatchServiceDTO() {
    }

    public MatchServiceDTO(String name, Long tournamentId, Long serverId, Long gameMapId, Long gametypeId, Long localTeamId, Long guestTeamId) {
        this.name = name;
        this.tournamentId = tournamentId;
        this.serverId = serverId;
        this.gameMapId = gameMapId;
        this.gametypeId = gametypeId;
        this.localTeamId = localTeamId;
        this.guestTeamId = guestTeamId;
    }
}
