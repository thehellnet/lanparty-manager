package org.thehellnet.lanparty.manager.model.persistence;

import org.thehellnet.lanparty.manager.model.constant.PaneMode;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pane")
public class Pane extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pane_id_seq")
    @SequenceGenerator(name = "pane_id_seq", sequenceName = "pane_id_seq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "showcase_id", nullable = false)
    private Showcase showcase;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaneMode mode = PaneMode.MATCHES;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    public Pane() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Showcase getShowcase() {
        return showcase;
    }

    public void setShowcase(Showcase showcase) {
        this.showcase = showcase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PaneMode getMode() {
        return mode;
    }

    public void setMode(PaneMode mode) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pane pane = (Pane) o;
        return Id.equals(pane.Id) &&
                showcase.equals(pane.showcase) &&
                name.equals(pane.name) &&
                mode == pane.mode &&
                Objects.equals(tournament, pane.tournament) &&
                Objects.equals(match, pane.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Id);
    }

    @Override
    public String toString() {
        return name;
    }
}
