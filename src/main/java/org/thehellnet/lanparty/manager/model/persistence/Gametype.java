package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gametype",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        })
public class Gametype extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gametype_id_seq")
    @SequenceGenerator(name = "gametype_id_seq", sequenceName = "gametype_id_seq")
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "gametype", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                name.equals(gametype.name) &&
                gameGametypes.equals(gametype.gameGametypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
