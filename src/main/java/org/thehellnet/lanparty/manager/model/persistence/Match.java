package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.thehellnet.lanparty.manager.model.constant.MatchStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "match")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Match implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq")
    @SequenceGenerator(name = "match_id_seq", sequenceName = "match_id_seq")
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Tournament tournament;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Basic
    @Column(name = "play_order", nullable = false)
    private Integer playOrder = 0;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Server server;

    @ManyToOne
    @JoinColumn(name = "gamemap_id")
    @JsonIdentityReference(alwaysAsId = true)
    private GameMap gameMap;

    @ManyToOne
    @JoinColumn(name = "gametype_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Gametype gametype;

    @ManyToOne
    @JoinColumn(name = "local_team_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Team localTeam;

    @ManyToOne
    @JoinColumn(name = "guest_team_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Team guestTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public Gametype getGametype() {
        return gametype;
    }

    public void setGametype(Gametype gametype) {
        this.gametype = gametype;
    }

    public Team getLocalTeam() {
        return localTeam;
    }

    public void setLocalTeam(Team localTeam) {
        this.localTeam = localTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id.equals(match.id) &&
                name.equals(match.name) &&
                tournament.equals(match.tournament) &&
                status == match.status &&
                playOrder.equals(match.playOrder) &&
                Objects.equals(server, match.server) &&
                Objects.equals(gameMap, match.gameMap) &&
                Objects.equals(gametype, match.gametype) &&
                Objects.equals(localTeam, match.localTeam) &&
                Objects.equals(guestTeam, match.guestTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
