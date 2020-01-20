package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEnabledTrueAndEmail(String email);

    AppUser findByEnabledFalseAndEmailAndConfirmCode(String email, String conformCode);

    AppUser findByBarcode(String barcode);
}
