package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gametype",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_uniq", columnNames = {"name"})
        })
@Description("Gametype")
public class Gametype extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gametype_id_seq")
    @SequenceGenerator(name = "gametype_id_seq", sequenceName = "gametype_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('gametype_id_seq')")
    @Description("Primary key")
    private Long id;

    @OneToMany(mappedBy = "gametype", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Related games")
    private List<GameGametype> gameGametypes = new ArrayList<>();

    public Gametype() {
    }

    public Gametype(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GameGametype> getGameGametypes() {
        return gameGametypes;
    }

    public void setGameGametypes(List<GameGametype> gameGametypes) {
        this.gameGametypes = gameGametypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gametype gametype = (Gametype) o;
        return id.equals(gametype.id) &&
                gameGametypes.equals(gametype.gameGametypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
