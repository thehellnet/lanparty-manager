package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cfg")
public class Cfg implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfg_id_seq")
    @SequenceGenerator(name = "cfg_id_seq", sequenceName = "cfg_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Basic
    @Column(name = "cfg", nullable = false, length = 1048576)
    private String cfg = "";

    public Cfg() {
    }

    public Cfg(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Cfg(Player player, Game game, String cfg) {
        this.player = player;
        this.game = game;
        this.cfg = cfg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cfg cfg1 = (Cfg) o;
        return id.equals(cfg1.id) &&
                player.equals(cfg1.player) &&
                game.equals(cfg1.game) &&
                cfg.equals(cfg1.cfg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", player, game);
    }
}
