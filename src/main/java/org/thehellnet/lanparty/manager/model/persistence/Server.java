package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "server",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"address", "port"})
        })
public class Server extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_id_seq")
    @SequenceGenerator(name = "server_id_seq", sequenceName = "server_id_seq")
    private Long Id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "port", nullable = false)
    private Integer port;

    @Basic
    @Column(name = "rcon_password")
    private String rconPassword;

    @Basic
    @Column(name = "log_file")
    private String logFile;

    @Basic
    @Column(name = "log_parsing_enabled", nullable = false)
    private Boolean logParsingEnabled = false;

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
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Server server = (Server) o;
        return Id.equals(server.Id) &&
                tag.equals(server.tag) &&
                Objects.equals(name, server.name) &&
                game.equals(server.game) &&
                address.equals(server.address) &&
                port.equals(server.port) &&
                Objects.equals(rconPassword, server.rconPassword) &&
                Objects.equals(logFile, server.logFile) &&
                logParsingEnabled.equals(server.logParsingEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Id);
    }

    @Override
    public String toString() {
        return name != null ? name : tag;
    }
}
