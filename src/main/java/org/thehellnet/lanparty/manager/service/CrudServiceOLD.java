package org.thehellnet.lanparty.manager.service;

import org.thehellnet.lanparty.manager.model.dto.service.ServiceDTO;

import java.util.List;

@Deprecated
public interface CrudServiceOLD<T, D extends ServiceDTO> {

    T create(D dto);

    T read(Long id);

    List<T> readAll();

    T update(Long id, D dto);

    void delete(Long id);

    T findById(Long id);
}
