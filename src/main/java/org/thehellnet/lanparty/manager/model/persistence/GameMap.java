package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gamemap",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tag", "game_id"})
        })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GameMap implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamemap_id_seq")
    @SequenceGenerator(name = "gamemap_id_seq", sequenceName = "gamemap_id_seq")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false)
    private String tag;

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Game game;

    @Basic
    @Column(name = "stock")
    private Boolean stock = false;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        GameMap gameMap = (GameMap) o;
        return id.equals(gameMap.id) &&
                tag.equals(gameMap.tag) &&
                Objects.equals(name, gameMap.name) &&
                game.equals(gameMap.game) &&
                stock.equals(gameMap.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name != null ? name : tag;
    }
}
