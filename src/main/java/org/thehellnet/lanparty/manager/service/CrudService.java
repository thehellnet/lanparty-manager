package org.thehellnet.lanparty.manager.service;

import java.util.List;
import java.util.Map;

public interface CrudService<T> {

    T create(Map<String, Object> dto);

    T read(Long id);

    List<T> readAll();

    T update(Long id, Map<String, Object> dto);

    void delete(Long id);

    T findById(Long id);
}
