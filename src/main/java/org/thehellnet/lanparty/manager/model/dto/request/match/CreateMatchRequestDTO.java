package org.thehellnet.lanparty.manager.model.dto.request.match;

public class CreateMatchRequestDTO extends MatchRequestDTO {

    public String name;
    public Long tournament;
    public Long server;
    public Long gameMap;
    public Long gametype;
    public Long localTeam;
    public Long guestTeam;
}
