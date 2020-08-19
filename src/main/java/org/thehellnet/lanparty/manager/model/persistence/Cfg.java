package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cfg")
@Description("CFG")
public class Cfg extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfg_id_seq")
    @SequenceGenerator(name = "cfg_id_seq", sequenceName = "cfg_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('cfg_id_seq')")
    @Description("Primary key")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @Description("Related player")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @Description("Related game")
    private Game game;

    @Basic
    @Column(name = "cfg_content", nullable = false, length = 1048576)
    @ColumnDefault("''")
    @Description("Cfg content")
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

    public String getCfgContent() {
        return cfgContent;
    }

    public void setCfgContent(String cfg) {
        this.cfgContent = cfg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cfg cfg = (Cfg) o;
        return id.equals(cfg.id) &&
                player.equals(cfg.player) &&
                game.equals(cfg.game) &&
                cfgContent.equals(cfg.cfgContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", player, game);
    }
}
