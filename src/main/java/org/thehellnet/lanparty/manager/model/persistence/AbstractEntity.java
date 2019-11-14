package org.thehellnet.lanparty.manager.model.persistence;

import java.io.Serializable;

public abstract class AbstractEntity<T extends AbstractEntity> implements Serializable {

    public abstract void updateFromEntity(T dto);

    public abstract void setId(Long id);
}
