package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "showcase")
@Description("Showcase")
public class Showcase extends AbstractEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "showcase_id_seq")
    @SequenceGenerator(name = "showcase_id_seq", sequenceName = "showcase_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('showcase_id_seq')")
    @Description("Primary key")
    private Long id;

    @Basic
    @Column(name = "tag", nullable = false, unique = true)
    @Description("Unique tag")
    private String tag;

    @Basic
    @Column(name = "connected", nullable = false)
    @ColumnDefault("FALSE")
    @Description("If showcase is connected or not")
    private Boolean connected = Boolean.FALSE;

    @Basic
    @Column(name = "last_address")
    @Description("Last IP Addresses")
    private String lastAddress;

    @Basic
    @Column(name = "last_contact")
    @Description("Date & Time of last contact")
    private DateTime lastContact;

    @OneToMany(mappedBy = "showcase", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Description("Panes for this Showcase")
    private List<Pane> panes = new ArrayList<>();

    public Showcase() {
    }

    public Showcase(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public DateTime getLastContact() {
        return lastContact;
    }

    public void setLastContact(DateTime lastContact) {
        this.lastContact = lastContact;
    }

    public List<Pane> getPanes() {
        return panes;
    }

    public void setPanes(List<Pane> panes) {
        this.panes = panes;
    }

    public void updateLastContact() {
        lastContact = DateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Showcase showcase = (Showcase) o;
        return id.equals(showcase.id) &&
                tag.equals(showcase.tag) &&
                Objects.equals(name, showcase.name) &&
                connected.equals(showcase.connected) &&
                Objects.equals(lastAddress, showcase.lastAddress) &&
                Objects.equals(lastContact, showcase.lastContact) &&
                panes.equals(showcase.panes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
