package org.thehellnet.lanparty.manager.model.dto.service;

public class PlayerServiceDTO extends ServiceDTO {

    public String nickname;
    public Long appUserId;
    public Long teamId;

    public PlayerServiceDTO() {
    }

    public PlayerServiceDTO(String nickname, Long appUserId, Long teamId) {
        this.nickname = nickname;
        this.appUserId = appUserId;
        this.teamId = teamId;
    }
}
