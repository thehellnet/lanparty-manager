package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.MatchStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "match")
public class Match extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq")
    @SequenceGenerator(name = "match_id_seq", sequenceName = "match_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Basic
    @Column(name = "scheduled_start_ts")
    private DateTime scheduledStartTs;

    @Basic
    @Column(name = "scheduled_end_ts")
    private DateTime scheduledEndTs;

    @Basic
    @Column(name = "start_ts")
    private DateTime startTs;

    @Basic
    @Column(name = "end_ts")
    private DateTime endTs;

    @Basic
    @Column(name = "play_order", nullable = false)
    private Integer playOrder = 0;

    @Basic
    @Column(name = "level", nullable = false)
    private Integer level = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "match", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<MatchParent> matchParents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @OneToOne(mappedBy = "match")
    private ServerMatch serverMatch;

    @ManyToOne
    @JoinColumn(name = "gamemap_id")
    private GameMap gameMap;

    @ManyToOne
    @JoinColumn(name = "gametype_id")
    private Gametype gametype;

    @ManyToOne
    @JoinColumn(name = "local_team_id")
    private Team localTeam;

    @ManyToOne
    @JoinColumn(name = "guest_team_id")
    private Team guestTeam;

    public Match() {
    }

    public Match(String name) {
        this.name = name;
    }

    public Match(String name, Tournament tournament, Team localTeam, Team guestTeam) {
        this.name = name;
        this.tournament = tournament;
        this.localTeam = localTeam;
        this.guestTeam = guestTeam;
    }

    public Match(String name, Tournament tournament, Server server, GameMap gameMap, Gametype gametype, Team localTeam, Team guestTeam) {
        this.name = name;
        this.tournament = tournament;
        this.server = server;
        this.gameMap = gameMap;
        this.gametype = gametype;
        this.localTeam = localTeam;
        this.guestTeam = guestTeam;
    }

    public Match(Tournament tournament, Team localTeam, Team guestTeam, int level) {
        this.tournament = tournament;
        this.localTeam = localTeam;
        this.guestTeam = guestTeam;
        this.level = level;
    }

    public Match(Tournament tournament, int level) {
        this.tournament = tournament;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DateTime getScheduledStartTs() {
        return scheduledStartTs;
    }

    public void setScheduledStartTs(DateTime scheduledStartTs) {
        this.scheduledStartTs = scheduledStartTs;
    }

    public DateTime getScheduledEndTs() {
        return scheduledEndTs;
    }

    public void setScheduledEndTs(DateTime scheduledEndTs) {
        this.scheduledEndTs = scheduledEndTs;
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

    public Integer getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<MatchParent> getMatchParents() {
        return matchParents;
    }

    public void setMatchParents(List<MatchParent> matchParents) {
        this.matchParents = matchParents;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ServerMatch getServerMatch() {
        return serverMatch;
    }

    public void setServerMatch(ServerMatch match) {
        this.serverMatch = match;
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

    public void computeName() {
        name = String.format("%s - %s vs %s", tournament, localTeam, guestTeam);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Match match = (Match) o;
        return id.equals(match.id) &&
                name.equals(match.name) &&
                tournament.equals(match.tournament) &&
                status == match.status &&
                Objects.equals(scheduledStartTs, match.scheduledStartTs) &&
                Objects.equals(scheduledEndTs, match.scheduledEndTs) &&
                Objects.equals(startTs, match.startTs) &&
                Objects.equals(endTs, match.endTs) &&
                playOrder.equals(match.playOrder) &&
                level.equals(match.level) &&
                matchParents.equals(match.matchParents) &&
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
}
