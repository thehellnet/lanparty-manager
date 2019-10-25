package org.thehellnet.lanparty.manager.model.dto.service;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.ShowcaseMode;

public class ShowcaseServiceDTO extends ServiceDTO {

    public String tag;
    public String name;
    public ShowcaseMode mode;
    public Long tournamentId;
    public Long matchId;
    public String lastAddress;
    public DateTime lastContact;

    public ShowcaseServiceDTO() {
    }

    public ShowcaseServiceDTO(String tag, String name, ShowcaseMode mode, Long tournamentId, Long matchId, String lastAddress, DateTime lastContact) {
        this.tag = tag;
        this.name = name;
        this.mode = mode;
        this.tournamentId = tournamentId;
        this.matchId = matchId;
        this.lastAddress = lastAddress;
        this.lastContact = lastContact;
    }
}
