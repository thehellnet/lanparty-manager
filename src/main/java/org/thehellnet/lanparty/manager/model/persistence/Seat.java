package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "seat",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_tournament_uniq", columnNames = {"name", "tournament_id"}),
                @UniqueConstraint(name = "guid_tournament_uniq", columnNames = {"guid", "tournament_id"}),
                @UniqueConstraint(name = "ip_address_uniq", columnNames = {"ip_address"})
        }
)
@Description("Tournament seat")
public class Seat extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_id_seq")
    @SequenceGenerator(name = "seat_id_seq", sequenceName = "seat_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('seat_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "ip_address", nullable = false)
    @Description("IP Address")
    private String ipAddress;

    @Basic
    @Column(name = "guid")
    @Description("Seat GUID")
    private String guid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    @Description("Related tournament")
    private Tournament tournament;

    @Basic
    @Column(name = "last_contact", nullable = false)
    @ColumnDefault("now()")
    @Description("Date & Time of last contact")
    private DateTime lastContact = new DateTime();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @Description("Current player")
    private Player player;

    public Seat() {
    }

    public Seat(String name) {
        this.name = name;
    }

    public Seat(String name, String ipAddress, Tournament tournament) {
        this();
        this.name = name;
        this.ipAddress = ipAddress;
        this.tournament = tournament;
    }

    public Seat(String name, String ipAddress, Tournament tournament, Player player) {
        this(name, ipAddress, tournament);
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public DateTime getLastContact() {
        return lastContact;
    }

    public void setLastContact(DateTime lastContact) {
        this.lastContact = lastContact;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void updateLastContact() {
        lastContact = DateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Seat seat = (Seat) o;
        return id.equals(seat.id) &&
                name.equals(seat.name) &&
                Objects.equals(guid, seat.guid) &&
                ipAddress.equals(seat.ipAddress) &&
                tournament.equals(seat.tournament) &&
                lastContact.equals(seat.lastContact) &&
                Objects.equals(player, seat.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
