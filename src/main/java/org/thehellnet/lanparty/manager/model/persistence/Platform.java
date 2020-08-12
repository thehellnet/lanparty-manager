package org.thehellnet.lanparty.manager.model.persistence;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "platform",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"})
        })
public class Platform extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pane_id_seq")
    @SequenceGenerator(name = "pane_id_seq", sequenceName = "pane_id_seq")
    private Long Id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @OneToMany(mappedBy = "platform", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Game> games = new ArrayList<>();

    public Platform() {
    }

    public Platform(String tag) {
        this.tag = tag;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Platform platform = (Platform) o;
        return Id.equals(platform.Id) &&
                tag.equals(platform.tag) &&
                name.equals(platform.name) &&
                games.equals(platform.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Id);
    }
}
