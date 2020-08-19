package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "server",
        uniqueConstraints = {
                @UniqueConstraint(name = "address_port_uniq", columnNames = {"address", "port"})
        })
@Description("Game server")
public class Server extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_id_seq")
    @SequenceGenerator(name = "server_id_seq", sequenceName = "server_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('server_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    @Description("Server unique tag")
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @Description("Related game")
    private Game game;

    @Basic
    @Column(name = "address", nullable = false)
    @Description("Server IP address")
    private String address;

    @Basic
    @Column(name = "port", nullable = false)
    @Description("Server port")
    private Integer port;

    @Basic
    @Column(name = "rcon_password")
    @Description("RCON password")
    private String rconPassword;

    @Basic
    @Column(name = "log_file")
    @Description("Log file path")
    private String logFile;

    @Basic
    @Column(name = "log_parsing_enabled", nullable = false)
    @ColumnDefault("FALSE")
    @Description("Enables log parsing")
    private Boolean logParsingEnabled = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_binary_id")
    @Description("Related Server binary")
    private ServerBinary serverBinary;

    @Basic
    @Column(name = "server_binary_enabled", nullable = false)
    @ColumnDefault("FALSE")
    @Description("Enable running server instance")
    private Boolean serverBinaryEnabled = Boolean.FALSE;

    @Basic
    @Column(name = "server_binary_extra_commands", nullable = false, length = 1048576)
    @ColumnDefault("''")
    @Description("Extra commands for instance")
    private String serverBinaryExtraCommands = "";

    public Server() {
    }

    public Server(String tag, Game game, String address, Integer port) {
        this.tag = tag;
        this.game = game;
        this.address = address;
        this.port = port;
    }

    public Server(String tag, String name, Game game, String address, Integer port, String rconPassword, String logFile, Boolean logParsingEnabled) {
        this.tag = tag;
        this.name = name;
        this.game = game;
        this.address = address;
        this.port = port;
        this.rconPassword = rconPassword;
        this.logFile = logFile;
        this.logParsingEnabled = logParsingEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRconPassword() {
        return rconPassword;
    }

    public void setRconPassword(String rconPassword) {
        this.rconPassword = rconPassword;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public Boolean getLogParsingEnabled() {
        return logParsingEnabled;
    }

    public void setLogParsingEnabled(Boolean logParsingEnabled) {
        this.logParsingEnabled = logParsingEnabled;
    }

    public ServerBinary getServerBinary() {
        return serverBinary;
    }

    public void setServerBinary(ServerBinary serverBinary) {
        this.serverBinary = serverBinary;
    }

    public Boolean getServerBinaryEnabled() {
        return serverBinaryEnabled;
    }

    public void setServerBinaryEnabled(Boolean serverBinaryEnabled) {
        this.serverBinaryEnabled = serverBinaryEnabled;
    }

    public String getServerBinaryExtraCommands() {
        return serverBinaryExtraCommands;
    }

    public void setServerBinaryExtraCommands(String serverBinaryExtraCommands) {
        this.serverBinaryExtraCommands = serverBinaryExtraCommands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Server server = (Server) o;
        return id.equals(server.id) &&
                tag.equals(server.tag) &&
                Objects.equals(name, server.name) &&
                game.equals(server.game) &&
                address.equals(server.address) &&
                port.equals(server.port) &&
                Objects.equals(rconPassword, server.rconPassword) &&
                Objects.equals(logFile, server.logFile) &&
                Objects.equals(serverBinary, server.serverBinary) &&
                serverBinaryEnabled.equals(server.serverBinaryEnabled) &&
                serverBinaryExtraCommands.equals(server.serverBinaryExtraCommands) &&
                logParsingEnabled.equals(server.logParsingEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return name.length() > 0 ? name : tag;
    }
}
