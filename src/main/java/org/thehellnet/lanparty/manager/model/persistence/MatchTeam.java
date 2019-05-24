package org.thehellnet.lanparty.manager.model.persistence;

import org.thehellnet.lanparty.manager.model.constant.MatchTeamResult;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "match_team")
public class MatchTeam implements Serializable {

    @EmbeddedId
    private MatchTeamId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("matchId")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    private Team team;

    @Basic
    @Column(name = "play_order", nullable = false)
    private Integer playOrder = 0;

    @Basic
    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Basic
    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchTeamResult result = MatchTeamResult.NOT_PLAYED;

    public MatchTeamId getId() {
        return id;
    }

    public void setId(MatchTeamId id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public MatchTeamResult getResult() {
        return result;
    }

    public void setResult(MatchTeamResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchTeam matchTeam = (MatchTeam) o;
        return id.equals(matchTeam.id) &&
                match.equals(matchTeam.match) &&
                team.equals(matchTeam.team) &&
                playOrder.equals(matchTeam.playOrder) &&
                score.equals(matchTeam.score) &&
                result == matchTeam.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", match.toString(), team.toString());
    }
}
