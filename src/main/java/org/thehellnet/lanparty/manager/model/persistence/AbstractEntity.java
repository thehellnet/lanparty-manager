package org.thehellnet.lanparty.manager.model.persistence;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.thehellnet.lanparty.manager.model.persistence.annotation.Hidden;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Hidden
    @Basic
    @Column(name = "created_ts")
    @CreatedDate
    protected DateTime createdTs;

    @Hidden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy
    protected AppUser createdBy;

    @Hidden
    @Basic
    @Column(name = "last_modified_ts")
    @LastModifiedDate
    protected DateTime lastModifiedTs;

    @Hidden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    @LastModifiedBy
    protected AppUser lastModifiedBy;

    @Hidden
    @Basic
    @Column(name = "active", nullable = false)
    protected Boolean active = Boolean.TRUE;

    @Hidden
    @Transient
    protected String friendlyName;

    @PostLoad
    protected void postLoad() {
        this.friendlyName = this.toString();
    }

    public DateTime getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(DateTime createdTs) {
        this.createdTs = createdTs;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getLastModifiedTs() {
        return lastModifiedTs;
    }

    public void setLastModifiedTs(DateTime lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    public AppUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(AppUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return createdTs.equals(that.createdTs) &&
                createdBy.equals(that.createdBy) &&
                Objects.equals(lastModifiedTs, that.lastModifiedTs) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
                active.equals(that.active);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
