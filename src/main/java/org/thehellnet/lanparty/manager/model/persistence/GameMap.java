package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "game_map",
        uniqueConstraints = {
                @UniqueConstraint(name = "game_tag_uniq", columnNames = {"game_id", "tag"})
        })
@Description("Game map")
public class GameMap extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_map_id_seq")
    @SequenceGenerator(name = "game_map_id_seq", sequenceName = "game_map_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('game_map_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false)
    @Description("Map tag")
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @Description("Related game")
    private Game game;

    @Basic
    @Column(name = "stock", nullable = false)
    @ColumnDefault("FALSE")
    @Description("If map is stock or user-added")
    private Boolean stock = Boolean.FALSE;

    public GameMap() {
    }

    public GameMap(String tag) {
        this.tag = tag;
    }

    public GameMap(String tag, Game game) {
        this.tag = tag;
        this.game = game;
    }

    public GameMap(String tag, String name, Game game) {
        this.tag = tag;
        this.name = name;
        this.game = game;
    }

    public GameMap(String tag, String name, Game game, Boolean stock) {
        this.tag = tag;
        this.name = name;
        this.game = game;
        this.stock = stock;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Boolean getStock() {
        return stock;
    }

    public void setStock(Boolean stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameMap gameMap = (GameMap) o;
        return id.equals(gameMap.id) &&
                tag.equals(gameMap.tag) &&
                Objects.equals(name, gameMap.name) &&
                game.equals(gameMap.game) &&
                stock.equals(gameMap.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return name.length() > 0 ? name : tag;
    }
}
