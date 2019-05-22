package org.thehellnet.lanparty.manager.model.persistence;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "player_team")
public class PlayerTeam implements Serializable {

    @EmbeddedId
    private PlayerTeamId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerId")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    private Team team;

    public PlayerTeamId getId() {
        return id;
    }

    public void setId(PlayerTeamId id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTeam that = (PlayerTeam) o;
        return player.equals(that.player) &&
                team.equals(that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, team);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", team.toString(), player.toString());
    }
}
