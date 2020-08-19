package org.thehellnet.lanparty.manager.model.persistence;


import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "platform",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_uniq", columnNames = {"name"})
        })
@Description("Gaming platform")
public class Platform extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_id_seq")
    @SequenceGenerator(name = "platform_id_seq", sequenceName = "platform_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('platform_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    @Description("Platform unique tag")
    private String tag;

    @OneToMany(mappedBy = "platform", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Available games")
    private List<Game> games = new ArrayList<>();

    public Platform() {
    }

    public Platform(String tag) {
        this.tag = tag;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        return id.equals(platform.id) &&
                tag.equals(platform.tag) &&
                games.equals(platform.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
