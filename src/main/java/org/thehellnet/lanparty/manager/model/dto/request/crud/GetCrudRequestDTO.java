package org.thehellnet.lanparty.manager.model.dto.request.crud;

public class GetCrudRequestDTO extends CrudRequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
