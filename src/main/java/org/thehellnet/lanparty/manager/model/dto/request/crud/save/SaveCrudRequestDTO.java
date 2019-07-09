package org.thehellnet.lanparty.manager.model.dto.request.crud.save;

import org.thehellnet.lanparty.manager.model.dto.request.crud.CrudRequestDTO;

public abstract class SaveCrudRequestDTO extends CrudRequestDTO {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
