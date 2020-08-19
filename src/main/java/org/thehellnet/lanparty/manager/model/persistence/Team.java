package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "team",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_uniq", columnNames = {"name"})
        })
@Description("Tournament team")
public class Team extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('team_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    @Description("Related Tournament")
    private Tournament tournament;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related Players")
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "localTeam", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related teams as locals")
    private List<Match> localMatches = new ArrayList<>();

    @OneToMany(mappedBy = "guestTeam", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related teams as guests")
    private List<Match> guestMatches = new ArrayList<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Match> getLocalMatches() {
        return localMatches;
    }

    public void setLocalMatches(List<Match> localMatches) {
        this.localMatches = localMatches;
    }

    public List<Match> getGuestMatches() {
        return guestMatches;
    }

    public void setGuestMatches(List<Match> guestMatches) {
        this.guestMatches = guestMatches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
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
        return Objects.hash(super.hashCode(), id);
    }
}
