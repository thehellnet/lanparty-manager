package org.thehellnet.lanparty.manager.model.dto.request.crud;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public class DeleteCrudRequestDTO extends RequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}