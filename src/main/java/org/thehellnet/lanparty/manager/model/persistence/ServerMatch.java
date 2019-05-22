package org.thehellnet.lanparty.manager.model.persistence;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "server_match")
public class ServerMatch implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_match_id_seq")
    @SequenceGenerator(name = "server_match_id_seq", sequenceName = "server_match_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @OneToOne(mappedBy = "serverMatch")
    @JoinColumn(name = "match_id")
    private Match match;

    @Basic
    @Column(name = "ts_start", nullable = false)
    private DateTime tsStart = new DateTime();

    @Basic
    @Column(name = "ts_end")
    private DateTime tsEnd = new DateTime();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public DateTime getTsStart() {
        return tsStart;
    }

    public void setTsStart(DateTime tsStart) {
        this.tsStart = tsStart;
    }

    public DateTime getTsEnd() {
        return tsEnd;
    }

    public void setTsEnd(DateTime tsEnd) {
        this.tsEnd = tsEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerMatch that = (ServerMatch) o;
        return id.equals(that.id) &&
                server.equals(that.server) &&
                Objects.equals(match, that.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, server, match);
    }

    @Override
    public String toString() {
        return server.toString();
    }
}
