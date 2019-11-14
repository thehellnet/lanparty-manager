package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.thehellnet.lanparty.manager.model.constant.TournamentStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tournament")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class Tournament extends AbstractEntity<Tournament> {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_id_seq")
    @SequenceGenerator(name = "tournament_id_seq", sequenceName = "tournament_id_seq")
    private Long Id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TournamentStatus status = TournamentStatus.SCHEDULED;

    @Basic
    @Column(name = "cfg", nullable = false, length = 1048576)
    private String cfg = "";

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Seat> seats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Match> matches = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Team> teams = new HashSet<>();

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
        this.cfg = cfg;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    @Override
    public void updateFromEntity(Tournament dto) {
        name = dto.name;
        game = dto.game;
        status = dto.status;
        cfg = dto.cfg;
        seats = dto.seats;
        matches = dto.matches;
        teams = dto.teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Id.equals(that.Id) &&
                name.equals(that.name) &&
                game.equals(that.game) &&
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
