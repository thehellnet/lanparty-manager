package org.thehellnet.lanparty.manager.model.dto.request.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public class AppUserGetRequestDTO extends RequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
