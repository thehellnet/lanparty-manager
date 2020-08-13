package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "game")
public class Game extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_id_seq")
    @SequenceGenerator(name = "game_id_seq", sequenceName = "game_id_seq")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<GameGametype> gameGametypes = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<GameMap> gameMaps = new ArrayList<>();

    public Game() {
    }

    public Game(String tag) {
        this.tag = tag;
    }

    public Game(String tag, String name) {
        this.tag = tag;
        this.name = name;
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

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public List<GameGametype> getGameGametypes() {
        return gameGametypes;
    }

    public void setGameGametypes(List<GameGametype> gameGametypes) {
        this.gameGametypes = gameGametypes;
    }

    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

    public void setGameMaps(List<GameMap> gameMaps) {
        this.gameMaps = gameMaps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Game game = (Game) o;
        return id.equals(game.id) &&
                tag.equals(game.tag) &&
                Objects.equals(name, game.name) &&
                platform.equals(game.platform) &&
                gameGametypes.equals(game.gameGametypes) &&
                gameMaps.equals(game.gameMaps);
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
