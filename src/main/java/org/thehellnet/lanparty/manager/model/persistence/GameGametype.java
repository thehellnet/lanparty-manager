package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "game_gametype",
        uniqueConstraints = {
                @UniqueConstraint(name = "game_gametype_uniq", columnNames = {"game_id", "gametype_id"})
        })
@Description("Implementation of gametype in game")
public class GameGametype extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_gametype_id_seq")
    @SequenceGenerator(name = "game_gametype_id_seq", sequenceName = "game_gametype_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('game_gametype_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @Description("Related game")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gametype_id", nullable = false)
    @Description("Related gametype")
    private Gametype gametype;

    @Basic
    @Column(name = "tag", nullable = false)
    @Description("Gametype tag used on cfg")
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
