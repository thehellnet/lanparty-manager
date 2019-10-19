package org.thehellnet.lanparty.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.dto.service.ServiceDTO;

import java.util.List;

public interface CrudService<T, D extends ServiceDTO> {

    T create(D dto);

    T read(Long id);

    List<T> readAll();

    T update(Long id, D dto);

    void delete(Long id);

    T findById(Long id);
}
