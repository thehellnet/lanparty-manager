package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.Description;
import org.thehellnet.lanparty.manager.model.persistence.annotation.Hidden;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

    @Hidden
    @Basic
    @Column(name = "created_ts")
    @CreatedDate
    @Description("Date & Time of record creation")
    protected LocalDateTime createdTs;

    @Hidden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy
    @Description("User which creates record")
    protected AppUser createdBy;

    @Hidden
    @Basic
    @Column(name = "last_modified_ts")
    @LastModifiedDate
    @Description("Date & Time of last modification")
    protected LocalDateTime lastModifiedTs;

    @Hidden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    @LastModifiedBy
    @Description("User which applyies last modification")
    protected AppUser lastModifiedBy;

    @Hidden
    @Basic
    @Column(name = "active", nullable = false)
    @Description("Record active")
    protected Boolean active = Boolean.TRUE;

    @Basic
    @Column(name = "name", nullable = false)
    @Description("Name")
    protected String name = "";

    @Hidden
    @Transient
    @Description("Friendly name")
    protected String friendlyName;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void updateName() {
        name = "";
    }

    @PostLoad
    protected void postLoad() {
        this.friendlyName = this.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(createdTs, that.createdTs) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(lastModifiedTs, that.lastModifiedTs) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
                active.equals(that.active) &&
                name.equals(that.name) &&
                Objects.equals(friendlyName, that.friendlyName);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
