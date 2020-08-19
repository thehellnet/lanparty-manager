package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "server_match")
@Description("Match in server")
public class ServerMatch extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_match_id_seq")
    @SequenceGenerator(name = "server_match_id_seq", sequenceName = "server_match_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('server_match_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    @Description("Related server")
    private Server server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gametype_id", nullable = false)
    @Description("Gametype")
    private Gametype gametype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gamemap_id", nullable = false)
    @Description("Map")
    private GameMap gameMap;

    @Basic
    @Column(name = "start_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of start")
    private DateTime startTs = DateTime.now();

    @Basic
    @Column(name = "end_ts")
    @Description("Date & Time of enf")
    private DateTime endTs;

    @OneToOne
    @JoinColumn(name = "match_id")
    @Description("Related match")
    private Match match;

    @JsonIgnore
    @OneToMany(mappedBy = "serverMatch", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Match Players")
    private List<ServerMatchPlayer> serverMatchPlayers = new ArrayList<>();

    public ServerMatch() {
    }

    public ServerMatch(Server server, Gametype gametype, GameMap gameMap) {
        this.server = server;
        this.gametype = gametype;
        this.gameMap = gameMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!super.equals(o)) return false;
        ServerMatch that = (ServerMatch) o;
        return id.equals(that.id) &&
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
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s", server, gametype, gameMap);
    }
}
