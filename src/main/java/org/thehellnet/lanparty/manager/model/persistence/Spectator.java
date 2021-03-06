package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "spectator",
        uniqueConstraints = {
                @UniqueConstraint(name = "address_port_uniq", columnNames = {"address", "port"})
        })
@Description("Spectator machine")
public class Spectator extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spectator_id_seq")
    @SequenceGenerator(name = "spectator_id_seq", sequenceName = "spectator_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('spectator_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "address", nullable = false)
    @Description("IP Address")
    private String address;

    @Basic
    @Column(name = "port", nullable = false)
    @ColumnDefault("62514")
    @Description("SpecTool TCP port")
    private Integer port = 62514;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    @Description("Related server")
    private Server server;

    @Basic
    @Column(name = "enabled", nullable = false)
    @ColumnDefault("TRUE")
    @Description("Enable management of this spectator")
    private Boolean enabled = Boolean.TRUE;

    @Basic
    @Column(name = "timeout_join_spectate", nullable = false)
    @ColumnDefault("5000")
    @Description("Timeout between match start and spectator join (ms)")
    private Integer timeoutJoinSpectate = 5000;

    @Basic
    @Column(name = "timeout_set_ready", nullable = false)
    @ColumnDefault("5000")
    @Description("Timeout between join and set-ready command (ms)")
    private Integer timeoutSetReady = 5000;

    @Basic
    @Column(name = "interval_next_player", nullable = false)
    @ColumnDefault("10000")
    @Description("Interval of player switching (ms)")
    private Integer intervalNextPlayer = 10000;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getTimeoutJoinSpectate() {
        return timeoutJoinSpectate;
    }

    public void setTimeoutJoinSpectate(Integer timeoutJoinSpectate) {
        this.timeoutJoinSpectate = timeoutJoinSpectate;
    }

    public Integer getTimeoutSetReady() {
        return timeoutSetReady;
    }

    public void setTimeoutSetReady(Integer timeoutSetReady) {
        this.timeoutSetReady = timeoutSetReady;
    }

    public Integer getIntervalNextPlayer() {
        return intervalNextPlayer;
    }

    public void setIntervalNextPlayer(Integer intervalNextPlayer) {
        this.intervalNextPlayer = intervalNextPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Spectator spectator = (Spectator) o;
        return id.equals(spectator.id) &&
                address.equals(spectator.address) &&
                port.equals(spectator.port) &&
                server.equals(spectator.server) &&
                enabled.equals(spectator.enabled) &&
                timeoutJoinSpectate.equals(spectator.timeoutJoinSpectate) &&
                timeoutSetReady.equals(spectator.timeoutSetReady) &&
                intervalNextPlayer.equals(spectator.intervalNextPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return String.format("%s:%d - %s", address, port, server);
    }
}
