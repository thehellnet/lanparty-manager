package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.dto.service.ServiceDTO;
import org.thehellnet.lanparty.manager.service.AbstractService;
import org.thehellnet.lanparty.manager.service.CrudServiceOLD;

import java.util.List;

@Deprecated
public abstract class AbstractCrudServiceOLD<T, D extends ServiceDTO, R extends JpaRepository<T, Long>>
        extends AbstractService
        implements CrudServiceOLD<T, D> {

    protected R repository;

    public AbstractCrudServiceOLD(R repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public T read(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<T> readAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        T record = findById(id);
        repository.delete(record);
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        T record = repository.findById(id).orElse(null);
        if (record == null) {
            throw new NotFoundException();
        }
        return record;
    }
}
