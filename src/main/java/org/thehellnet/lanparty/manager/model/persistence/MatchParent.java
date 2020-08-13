package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "match_parent")
public class MatchParent extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_parent_id_seq")
    @SequenceGenerator(name = "match_parent_id_seq", sequenceName = "match_parent_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Basic
    @Column(name = "order", nullable = false)
    private Integer order;

    public MatchParent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MatchParent that = (MatchParent) o;
        return id.equals(that.id) &&
                match.equals(that.match) &&
                order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
