package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService extends AbstractService {

    @Autowired
    public AppUserService(SeatRepository seatRepository,
                          PlayerRepository playerRepository,
                          AppUserRepository appUserRepository) {
        super(seatRepository, playerRepository, appUserRepository);
    }

    @Transactional(readOnly = true)
    public List<Role> getAppUserRoles(AppUser appUser) {
        List<Role> roles = new ArrayList<>();

        if (appUser != null) {
            appUser = appUserRepository.getOne(appUser.getId());
            roles.addAll(appUser.getRoles());
        }

        return roles;
    }

    @Transactional(readOnly = true)
    public AppUser findByBarcode(String barcode) {
        AppUser appUser = appUserRepository.findByBarcode(barcode);
        if (appUser == null) {
            throw new NotFoundException();
        }

        return appUser;
    }
}
