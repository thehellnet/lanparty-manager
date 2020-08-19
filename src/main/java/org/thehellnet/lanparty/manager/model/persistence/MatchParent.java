package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "match_parent")
@Description("Parent match")
public class MatchParent extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_parent_id_seq")
    @SequenceGenerator(name = "match_parent_id_seq", sequenceName = "match_parent_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('match_parent_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @Description("Related match")
    private Match match;

    @Basic
    @Column(name = "order", nullable = false)
    @ColumnDefault("0")
    @Description("Order")
    private Integer order = 0;

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
