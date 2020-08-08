package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq")
    private Long Id;

    @Basic
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    @JsonIgnore
    @OneToMany(mappedBy = "player", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Cfg> cfgs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
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
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
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

    public List<Cfg> getCfgs() {
        return cfgs;
    }

    public void setCfgs(List<Cfg> cfgs) {
        this.cfgs = cfgs;
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
        return Id.equals(player.Id) &&
                nickname.equals(player.nickname) &&
                Objects.equals(appUser, player.appUser) &&
                cfgs.equals(player.cfgs) &&
                team.equals(player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Id);
    }

    @Override
    public String toString() {
        return nickname;
    }
}
