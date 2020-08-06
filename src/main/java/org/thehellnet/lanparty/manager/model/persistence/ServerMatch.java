package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "server_match")
public class ServerMatch extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_match_id_seq")
    @SequenceGenerator(name = "server_match_id_seq", sequenceName = "server_match_id_seq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @ManyToOne
    @JoinColumn(name = "gametype_id", nullable = false)
    private Gametype gametype;

    @ManyToOne
    @JoinColumn(name = "gamemap_id", nullable = false)
    private GameMap gameMap;

    @Basic
    @Column(name = "start_ts", nullable = false)
    private DateTime startTs = DateTime.now();

    @Basic
    @Column(name = "end_ts")
    private DateTime endTs;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @JsonIgnore
    @OneToMany(mappedBy = "serverMatch", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ServerMatchPlayer> serverMatchPlayers = new ArrayList<>();

    public ServerMatch() {
    }

    public ServerMatch(Server server, Gametype gametype, GameMap gameMap) {
        this.server = server;
        this.gametype = gametype;
        this.gameMap = gameMap;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Gametype getGametype() {
        return gametype;
    }

    public void setGametype(Gametype gametype) {
        this.gametype = gametype;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public DateTime getStartTs() {
        return startTs;
    }

    public void setStartTs(DateTime startTs) {
        this.startTs = startTs;
    }

    public DateTime getEndTs() {
        return endTs;
    }

    public void setEndTs(DateTime endTs) {
        this.endTs = endTs;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<ServerMatchPlayer> getServerMatchPlayers() {
        return serverMatchPlayers;
    }

    public void setServerMatchPlayers(List<ServerMatchPlayer> serverMatchPlayers) {
        this.serverMatchPlayers = serverMatchPlayers;
    }

    public void close() {
        this.endTs = DateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerMatch that = (ServerMatch) o;
        return Id.equals(that.Id) &&
                server.equals(that.server) &&
                gametype.equals(that.gametype) &&
                gameMap.equals(that.gameMap) &&
                startTs.equals(that.startTs) &&
                Objects.equals(endTs, that.endTs) &&
                Objects.equals(match, that.match) &&
                serverMatchPlayers.equals(that.serverMatchPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", server, gametype, gameMap);
    }
}
