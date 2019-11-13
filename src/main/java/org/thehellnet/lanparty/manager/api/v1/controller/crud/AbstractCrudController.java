package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thehellnet.lanparty.manager.api.v1.controller.AbstractController;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

public abstract class AbstractCrudController<T, S extends JpaRepository>
        extends AbstractController
        implements CrudController<T> {

    protected final S repository;

    protected AbstractCrudController(S repository) {
        this.repository = repository;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public abstract ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody T dto);

    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public abstract ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id);

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public abstract ResponseEntity read(HttpServletRequest request, AppUser appUser);

    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public abstract ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody T dto);

    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public abstract ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id);

    @Transactional
    @SuppressWarnings("unchecked")
    protected ResponseEntity createEntity(T dto) {
        T entity = (T) repository.save(dto);
        return ResponseEntity.created(URI.create("")).body(entity);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    protected ResponseEntity readEntity(Long id) {
        T entity;

        try {
            entity = (T) repository.findById(id).orElseThrow();
        } catch (Throwable ignored) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok(entity);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    protected ResponseEntity readEntity() {
        List<T> entityList = repository.findAll();
        return ResponseEntity.ok(entityList);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    protected ResponseEntity updateEntity(Long id, T dto) {
        T entity = (T) repository
                .findById(id)
                .map(t -> {
                    updateImpl((T) t, dto);
                    return repository.save(t);
                }).orElseGet(() -> {
                    updateImplElse(id, dto);
                    return repository.save(dto);
                });
        return ResponseEntity.ok(entity);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    protected ResponseEntity deleteEntity(Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected abstract void updateImpl(T entity, T dto);

    protected abstract void updateImplElse(Long id, T dto);
}
