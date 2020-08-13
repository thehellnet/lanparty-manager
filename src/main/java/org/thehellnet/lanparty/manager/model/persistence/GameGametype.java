package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "game_gametype",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"game_id", "gametype_id"})
        })
public class GameGametype extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_gametype_id_seq")
    @SequenceGenerator(name = "game_gametype_id_seq", sequenceName = "game_gametype_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "gametype_id", nullable = false)
    private Gametype gametype;

    @Basic
    @Column(name = "tag", nullable = false)
    private String tag;

    public GameGametype() {
    }

    public GameGametype(Game game, Gametype gametype) {
        this.game = game;
        this.gametype = gametype;
    }

    public GameGametype(Game game, Gametype gametype, String tag) {
        this.game = game;
        this.gametype = gametype;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Gametype getGametype() {
        return gametype;
    }

    public void setGametype(Gametype gametype) {
        this.gametype = gametype;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameGametype that = (GameGametype) o;
        return id.equals(that.id) &&
                game.equals(that.game) &&
                gametype.equals(that.gametype) &&
                tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", game, gametype, tag);
    }
}
