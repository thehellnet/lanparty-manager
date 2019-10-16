package org.thehellnet.lanparty.manager.model.dto.request.server;

public class UpdateServerRequestDTO extends ServerRequestDTO {

    public String tag;
    public String name;
    public Long game;
    public String address;
    public Integer port;
    public String rconPassword;
    public String logFile;
    public Boolean logParsingEnabled;
}
