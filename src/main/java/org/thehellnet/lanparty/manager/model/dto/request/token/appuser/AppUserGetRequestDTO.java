package org.thehellnet.lanparty.manager.model.dto.request.token.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.token.TokenRequestDTO;

public class AppUserGetRequestDTO extends TokenRequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
