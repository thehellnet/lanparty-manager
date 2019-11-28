package org.thehellnet.lanparty.manager.model.persistence;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "seat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "tournament_id"}),
                @UniqueConstraint(columnNames = {"ip_address"}),
        }
)
public class Seat extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_id_seq")
    @SequenceGenerator(name = "seat_id_seq", sequenceName = "seat_id_seq")
    private Long Id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Basic
    @Column(name = "last_contact", nullable = false)
    private DateTime lastContact = new DateTime();

    @ManyToOne
    @JoinColumn(name = "player_id")
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
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
        Seat seat = (Seat) o;
        return Id.equals(seat.Id) &&
                name.equals(seat.name) &&
                ipAddress.equals(seat.ipAddress) &&
                tournament.equals(seat.tournament) &&
                lastContact.equals(seat.lastContact) &&
                Objects.equals(player, seat.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return name;
    }
}
