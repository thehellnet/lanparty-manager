package org.thehellnet.lanparty.manager.model.dto.service;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;

public class ShowcaseServiceDTO extends ServiceDTO {

    public String tag;
    public String name;
    public PaneMode mode;
    public Long tournamentId;
    public Long matchId;
    public String lastAddress;
    public DateTime lastContact;

    public ShowcaseServiceDTO() {
    }

    public ShowcaseServiceDTO(String tag, String name, PaneMode mode, Long tournamentId, Long matchId, String lastAddress, DateTime lastContact) {
        this.tag = tag;
        this.name = name;
        this.mode = mode;
        this.tournamentId = tournamentId;
        this.matchId = matchId;
        this.lastAddress = lastAddress;
        this.lastContact = lastContact;
    }
}
