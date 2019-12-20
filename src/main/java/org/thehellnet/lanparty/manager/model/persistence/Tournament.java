package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.TournamentStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tournament")
public class Tournament extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_id_seq")
    @SequenceGenerator(name = "tournament_id_seq", sequenceName = "tournament_id_seq")
    private Long Id;

    @Basic
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "start_datetime", nullable = false)
    private DateTime startDateTime;

    @Basic
    @Column(name = "end_datetime", nullable = false)
    private DateTime endDateTime;

    @Basic
    @Column(name = "registration_enabled", nullable = false)
    private Boolean registrationEnabled = true;

    @Basic
    @Column(name = "start_registration_datetime", nullable = false)
    private DateTime startRegistrationDateTime;

    @Basic
    @Column(name = "end_registration_datetime", nullable = false)
    private DateTime endRegistrationDateTime;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TournamentStatus status = TournamentStatus.SCHEDULED;

    @Basic
    @Column(name = "cfg", nullable = false, length = 1048576)
    private String cfg = "";

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Seat> seats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Match> matches = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean getRegistrationEnabled() {
        return registrationEnabled;
    }

    public void setRegistrationEnabled(Boolean registrationEnabled) {
        this.registrationEnabled = registrationEnabled;
    }

    public DateTime getStartRegistrationDateTime() {
        return startRegistrationDateTime;
    }

    public void setStartRegistrationDateTime(DateTime startRegistrationDateTime) {
        this.startRegistrationDateTime = startRegistrationDateTime;
    }

    public DateTime getEndRegistrationDateTime() {
        return endRegistrationDateTime;
    }

    public void setEndRegistrationDateTime(DateTime endRegistrationDateTime) {
        this.endRegistrationDateTime = endRegistrationDateTime;
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
        Tournament that = (Tournament) o;
        return Id.equals(that.Id) &&
                enabled == that.enabled &&
                name.equals(that.name) &&
                game.equals(that.game) &&
                startDateTime.equals(that.startDateTime) &&
                endDateTime.equals(that.endDateTime) &&
                registrationEnabled == that.registrationEnabled &&
                startRegistrationDateTime.equals(that.startRegistrationDateTime) &&
                endRegistrationDateTime.equals(that.endRegistrationDateTime) &&
                status == that.status &&
                cfg.equals(that.cfg) &&
                seats.equals(that.seats) &&
                matches.equals(that.matches) &&
                teams.equals(that.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return name;
    }
}
