package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.lanparty.manager.model.constant.MatchStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "match")
@Description("Match of tournament")
public class Match extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq")
    @SequenceGenerator(name = "match_id_seq", sequenceName = "match_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('match_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    @Description("Related tournament")
    private Tournament tournament;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SCHEDULED'")
    @Description("Match status")
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Basic
    @Column(name = "scheduled_start_ts")
    @Description("Scheduled date & time of start")
    private DateTime scheduledStartTs;

    @Basic
    @Column(name = "scheduled_end_ts")
    @Description("Scheduled date & time of end")
    private DateTime scheduledEndTs;

    @Basic
    @Column(name = "start_ts")
    @Description("Real date & time of start")
    private DateTime startTs;

    @Basic
    @Column(name = "end_ts")
    @Description("Real date & time of end")
    private DateTime endTs;

    @Basic
    @Column(name = "play_order", nullable = false)
    @ColumnDefault("0")
    @Description("Play order (lower first)")
    private Integer playOrder = 0;

    @Basic
    @Column(name = "level", nullable = false)
    @ColumnDefault("0")
    @Description("Level of nesting (in elimination or double round-robin, lower first)")
    private Integer level = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "match", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Parent matches (for elimination mode)")
    private List<MatchParent> matchParents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    @Description("Server in which this match is played")
    private Server server;

    @OneToOne(mappedBy = "match")
    @Description("Related Server match")
    private ServerMatch serverMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gamemap_id")
    @Description("Map of the match")
    private GameMap gameMap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gametype_id")
    @Description("Gametype of the match")
    private Gametype gametype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_team_id")
    @Description("Local team")
    private Team localTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_team_id")
    @Description("Guest team")
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
