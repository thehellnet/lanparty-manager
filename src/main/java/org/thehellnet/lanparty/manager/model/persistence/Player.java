package org.thehellnet.lanparty.manager.model.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "player")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq")
    private Long id;

    @Basic
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Basic
    @Column(name = "barcode")
    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    @JsonIdentityReference(alwaysAsId = true)
    private AppUser appUser;

    @OneToMany(mappedBy = "player", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Cfg> cfgs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Team team;

    public Player() {
    }

    public Player(String nickname, Team team) {
        this.nickname = nickname;
        this.team = team;
    }

    public Player(String nickname, String barcode, Team team) {
        this.nickname = nickname;
        this.barcode = barcode;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Set<Cfg> getCfgs() {
        return cfgs;
    }

    public void setCfgs(Set<Cfg> cfgs) {
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
        Player player = (Player) o;
        return id.equals(player.id) &&
                nickname.equals(player.nickname) &&
                Objects.equals(barcode, player.barcode) &&
                Objects.equals(appUser, player.appUser) &&
                cfgs.equals(player.cfgs) &&
                team.equals(player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nickname;
    }
}
