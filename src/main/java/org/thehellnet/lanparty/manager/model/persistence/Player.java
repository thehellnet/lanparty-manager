package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "player")
@Description("Player in team")
public class Player extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_id_seq")
    @SequenceGenerator(name = "player_id_seq", sequenceName = "player_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('player_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "nickname", nullable = false, unique = true)
    @Description("Nickname")
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    @Description("Related user")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @Description("Related team")
    private Team team;

    public Player() {
    }

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public Player(String nickname, Team team) {
        this.nickname = nickname;
        this.team = team;
    }

    public Player(String nickname, AppUser appUser, Team team) {
        this.nickname = nickname;
        this.appUser = appUser;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return id.equals(player.id) &&
                nickname.equals(player.nickname) &&
                Objects.equals(appUser, player.appUser) &&
                team.equals(player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return nickname;
    }
}
