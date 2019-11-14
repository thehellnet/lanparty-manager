package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cfg")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cfg extends AbstractEntity<Cfg> {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfg_id_seq")
    @SequenceGenerator(name = "cfg_id_seq", sequenceName = "cfg_id_seq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Game game;

    @Basic
    @Column(name = "cfg_content", nullable = false, length = 1048576)
    private String cfgContent = "";

    public Cfg() {
    }

    public Cfg(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Cfg(Player player, Game game, String cfgContent) {
        this.player = player;
        this.game = game;
        this.cfgContent = cfgContent;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getCfgContent() {
        return cfgContent;
    }

    public void setCfgContent(String cfg) {
        this.cfgContent = cfg;
    }

    @Override
    public void updateFromEntity(Cfg dto) {
        player = dto.player;
        game = dto.game;
        cfgContent = dto.cfgContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cfg cfg1 = (Cfg) o;
        return Id.equals(cfg1.Id) &&
                player.equals(cfg1.player) &&
                game.equals(cfg1.game) &&
                cfgContent.equals(cfg1.cfgContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", player, game);
    }
}
