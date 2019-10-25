package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.ShowcaseMode;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "showcase")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Showcase {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq")
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Basic
    @Column(name = "mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShowcaseMode mode = ShowcaseMode.MATCHES;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Match match;

    @Basic
    @Column(name = "last_address")
    private String lastAddress;

    @Basic
    @Column(name = "last_contact")
    private DateTime lastContact;

    public Showcase() {
    }

    public Showcase(String name) {
        this.name = name;
    }

    public Showcase(String name, Tournament tournament) {
        this.name = name;
        this.tournament = tournament;
    }

    public Showcase(String name, Match match) {
        this.name = name;
        this.match = match;
    }

    public Showcase(String name, ShowcaseMode mode, Tournament tournament, Match match, String lastAddress, DateTime lastContact) {
        this.name = name;
        this.mode = mode;
        this.tournament = tournament;
        this.match = match;
        this.lastAddress = lastAddress;
        this.lastContact = lastContact;
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

    public ShowcaseMode getMode() {
        return mode;
    }

    public void setMode(ShowcaseMode mode) {
        this.mode = mode;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public DateTime getLastContact() {
        return lastContact;
    }

    public void setLastContact(DateTime lastContact) {
        this.lastContact = lastContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showcase showcase = (Showcase) o;
        return id.equals(showcase.id) &&
                name.equals(showcase.name) &&
                mode == showcase.mode &&
                Objects.equals(tournament, showcase.tournament) &&
                Objects.equals(match, showcase.match) &&
                Objects.equals(lastAddress, showcase.lastAddress) &&
                Objects.equals(lastContact, showcase.lastContact);
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
