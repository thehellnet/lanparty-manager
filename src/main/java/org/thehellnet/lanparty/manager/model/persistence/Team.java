package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "team",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Team implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq")
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Tournament tournament;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy = "localTeam", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Match> localMatches = new HashSet<>();

    @OneToMany(mappedBy = "guestTeam", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Match> guestMatches = new HashSet<>();

    public Team() {
    }

    public Team(String name, Tournament tournament) {
        this.name = name;
        this.tournament = tournament;
    }

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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Match> getLocalMatches() {
        return localMatches;
    }

    public void setLocalMatches(Set<Match> localMatches) {
        this.localMatches = localMatches;
    }

    public Set<Match> getGuestMatches() {
        return guestMatches;
    }

    public void setGuestMatches(Set<Match> guestMatches) {
        this.guestMatches = guestMatches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id) &&
                name.equals(team.name) &&
                tournament.equals(team.tournament) &&
                players.equals(team.players) &&
                localMatches.equals(team.localMatches) &&
                guestMatches.equals(team.guestMatches);
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
