package org.thehellnet.lanparty.manager.model.dto.service;

public class SeatServiceDTO extends ServiceDTO {

    public String name;
    public String ipAddress;
    public Long tournamentId;
    public Long playerId;

    public SeatServiceDTO() {
    }

    public SeatServiceDTO(String name, String ipAddress, Long tournamentId, Long playerId) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.tournamentId = tournamentId;
        this.playerId = playerId;
    }
}
