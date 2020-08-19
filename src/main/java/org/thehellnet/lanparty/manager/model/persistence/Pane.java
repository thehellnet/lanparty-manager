package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pane",
        uniqueConstraints = {
                @UniqueConstraint(name = "showcase_mode_tournament_uniq", columnNames = {"showcase_id", "mode", "tournament_id"}),
                @UniqueConstraint(name = "showcase_mode_match_uniq", columnNames = {"showcase_id", "mode", "match_id"})
        })
@Description("Showcase pane")
public class Pane extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pane_id_seq")
    @SequenceGenerator(name = "pane_id_seq", sequenceName = "pane_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('pane_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showcase_id", nullable = false)
    @Description("Related showcase")
    private Showcase showcase;

    @Basic
    @Column(name = "mode", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MATCHES'")
    @Description("Pane mode")
    private PaneMode mode = PaneMode.MATCHES;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    @Description("Related tournament")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @Description("Related match")
    private Match match;

    @Basic
    @Column(name = "display_order", nullable = false)
    @ColumnDefault("0")
    @Description("Display order (lower first)")
    private Integer displayOrder = 0;

    @Basic
    @Column(name = "duration", nullable = false)
    @ColumnDefault("10")
    @Description("Duration")
    private Integer duration = 10;

    public Pane() {
    }

    public Pane(Showcase showcase, PaneMode mode, Tournament tournament) {
        this.showcase = showcase;
        this.mode = mode;
        this.tournament = tournament;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Showcase getShowcase() {
        return showcase;
    }

    public void setShowcase(Showcase showcase) {
        this.showcase = showcase;
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public void updateName() {
        name = String.format("%s - %s: %s", tournament, mode, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pane pane = (Pane) o;
        return id.equals(pane.id) &&
                showcase.equals(pane.showcase) &&
                name.equals(pane.name) &&
                mode == pane.mode &&
                Objects.equals(tournament, pane.tournament) &&
                Objects.equals(match, pane.match) &&
                displayOrder.equals(pane.displayOrder) &&
                duration.equals(pane.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
