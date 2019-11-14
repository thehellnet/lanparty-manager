package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "game_gametype",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"game_id", "gametype_id"})
        })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GameGametype extends AbstractEntity<GameGametype> {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_gametype_id_seq")
    @SequenceGenerator(name = "game_gametype_id_seq", sequenceName = "game_gametype_id_seq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "gametype_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Gametype gametype;

    @Basic
    @Column(name = "tag", nullable = false)
    private String tag;

    public GameGametype() {
    }

    public GameGametype(Game game, Gametype gametype, String tag) {
        this.game = game;
        this.gametype = gametype;
        this.tag = tag;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
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
    public void updateFromEntity(GameGametype dto) {
        game = dto.game;
        gametype = dto.gametype;
        tag = dto.tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameGametype that = (GameGametype) o;
        return Id.equals(that.Id) &&
                game.equals(that.game) &&
                gametype.equals(that.gametype) &&
                tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, game, gametype);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", game, gametype, tag);
    }
}
