package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;

import javax.servlet.http.HttpServletRequest;

public interface CrudController<T> {

    ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody T dto);

    ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id);

    ResponseEntity read(HttpServletRequest request, AppUser appUser);

    ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody T dto);

    ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id);
}
