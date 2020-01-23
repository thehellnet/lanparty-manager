package org.thehellnet.lanparty.manager.model.persistence;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Transient
    protected String friendlyName;

    public String getFriendlyName() {
        return friendlyName;
    }

    @PostLoad
    private void postLoad() {
        this.friendlyName = this.toString();
    }
}
