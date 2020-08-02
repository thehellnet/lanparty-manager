package org.thehellnet.lanparty.manager.model.persistence;

import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.thehellnet.lanparty.manager.model.persistence.annotation.Hidden;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @PostLoad
    protected void postLoad() {
        this.friendlyName = this.toString();
    }
}
