package org.thehellnet.lanparty.manager.model.persistence;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "server_match_player")
public class ServerMatchPlayer extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_match_id_seq")
    @SequenceGenerator(name = "server_match_id_seq", sequenceName = "server_match_id_seq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_match_id", nullable = false)
    private ServerMatch serverMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Basic
    @Column(name = "num")
    private Integer num;

    @Basic
    @Column(name = "guid")
    private String guid;

    @Basic
    @Column(name = "join_ts")
    private DateTime joinTs;

    @Basic
    @Column(name = "quit_ts")
    private DateTime quitTs;

    @Basic
    @Column(name = "kills")
    private Integer kills = 0;

    @Basic
    @Column(name = "deaths")
    private Integer deaths = 0;

    public ServerMatchPlayer() {
    }

    public ServerMatchPlayer(ServerMatch serverMatch) {
        this.serverMatch = serverMatch;
    }

    public ServerMatchPlayer(ServerMatch serverMatch, String guid, int num) {
        this.serverMatch = serverMatch;
        this.guid = guid;
        this.num = num;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public ServerMatch getServerMatch() {
        return serverMatch;
    }

    public void setServerMatch(ServerMatch serverMatch) {
        this.serverMatch = serverMatch;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public DateTime getJoinTs() {
        return joinTs;
    }

    public void setJoinTs(DateTime joinTs) {
        this.joinTs = joinTs;
    }

    public DateTime getQuitTs() {
        return quitTs;
    }

    public void setQuitTs(DateTime quitTs) {
        this.quitTs = quitTs;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public void addKill() {
        kills += 1;
    }

    public void addDeath() {
        deaths += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerMatchPlayer that = (ServerMatchPlayer) o;
        return Id.equals(that.Id) &&
                serverMatch.equals(that.serverMatch) &&
                Objects.equals(player, that.player) &&
                Objects.equals(num, that.num) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(joinTs, that.joinTs) &&
                Objects.equals(quitTs, that.quitTs) &&
                kills.equals(that.kills) &&
                deaths.equals(that.deaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return String.format("%d", num);
    }
}
