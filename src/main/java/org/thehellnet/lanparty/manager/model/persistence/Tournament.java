package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.lanparty.manager.model.constant.TournamentMode;
import org.thehellnet.lanparty.manager.model.constant.TournamentStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tournament")
@Description("Tournament")
public class Tournament extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_id_seq")
    @SequenceGenerator(name = "tournament_id_seq", sequenceName = "tournament_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('tournament_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "enabled", nullable = false)
    @Description("Enabled")
    private Boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @Description("Related game")
    private Game game;

    @Basic
    @Column(name = "start_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of start")
    private DateTime startTs = DateTime.now();

    @Basic
    @Column(name = "end_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of end")
    private DateTime endTs = DateTime.now();

    @Basic
    @Column(name = "registration_enabled", nullable = false)
    @Description("Enable registration")
    private Boolean registrationEnabled = true;

    @Basic
    @Column(name = "start_registration_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of start of registration period")
    private DateTime startRegistrationTs = DateTime.now();

    @Basic
    @Column(name = "end_registration_ts", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of end of registration period")
    private DateTime endRegistrationTs = DateTime.now();

    @Basic
    @Column(name = "tournament_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SINGLE_ELIMINATION'")
    @Description("Tournament mode")
    private TournamentMode mode = TournamentMode.SINGLE_ELIMINATION;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SCHEDULED'")
    @Description("Tournament status")
    private TournamentStatus status = TournamentStatus.SCHEDULED;

    @Basic
    @Column(name = "cfg", nullable = false, length = 1048576)
    @ColumnDefault("''")
    @Description("Tournament CFG")
    private String cfg = "";

    @Basic
    @Column(name = "override_cfg", nullable = false, length = 1048576)
    @ColumnDefault("''")
    @Description("CFG overrides")
    private String overrideCfg = "";

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related seats")
    private List<Seat> seats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related matches")
    private List<Match> matches = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related teams")
    private List<Team> teams = new ArrayList<>();

    public Tournament() {
    }

    public Tournament(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Tournament(String name, Game game, String cfg) {
        this.name = name;
        this.game = game;
        this.cfg = cfg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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

    public Boolean getRegistrationEnabled() {
        return registrationEnabled;
    }

    public void setRegistrationEnabled(Boolean registrationEnabled) {
        this.registrationEnabled = registrationEnabled;
    }

    public DateTime getStartRegistrationTs() {
        return startRegistrationTs;
    }

    public void setStartRegistrationTs(DateTime startRegistrationTs) {
        this.startRegistrationTs = startRegistrationTs;
    }

    public DateTime getEndRegistrationTs() {
        return endRegistrationTs;
    }

    public void setEndRegistrationTs(DateTime endRegistrationTs) {
        this.endRegistrationTs = endRegistrationTs;
    }

    public TournamentMode getMode() {
        return mode;
    }

    public void setMode(TournamentMode mode) {
        this.mode = mode;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg != null ? cfg : "";
    }

    public String getOverrideCfg() {
        return overrideCfg;
    }

    public void setOverrideCfg(String overrideCfg) {
        this.overrideCfg = overrideCfg;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tournament that = (Tournament) o;
        return id.equals(that.id) &&
                enabled.equals(that.enabled) &&
                name.equals(that.name) &&
                game.equals(that.game) &&
                startTs.equals(that.startTs) &&
                endTs.equals(that.endTs) &&
                registrationEnabled.equals(that.registrationEnabled) &&
                startRegistrationTs.equals(that.startRegistrationTs) &&
                endRegistrationTs.equals(that.endRegistrationTs) &&
                mode == that.mode &&
                status == that.status &&
                cfg.equals(that.cfg) &&
                overrideCfg.equals(that.overrideCfg) &&
                seats.equals(that.seats) &&
                matches.equals(that.matches) &&
                teams.equals(that.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
