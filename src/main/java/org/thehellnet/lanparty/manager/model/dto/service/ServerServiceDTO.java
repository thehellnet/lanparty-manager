package org.thehellnet.lanparty.manager.model.dto.service;

public class ServerServiceDTO extends ServiceDTO {

    public String tag;
    public String name;
    public Long gameId;
    public String address;
    public Integer port;
    public String rconPassword;
    public String logFile;
    public Boolean logParsingEnabled;

    public ServerServiceDTO() {
    }

    public ServerServiceDTO(String tag, String name, Long gameId, String address, Integer port, String rconPassword, String logFile, Boolean logParsingEnabled) {
        this.tag = tag;
        this.name = name;
        this.gameId = gameId;
        this.address = address;
        this.port = port;
        this.rconPassword = rconPassword;
        this.logFile = logFile;
        this.logParsingEnabled = logParsingEnabled;
    }
}
