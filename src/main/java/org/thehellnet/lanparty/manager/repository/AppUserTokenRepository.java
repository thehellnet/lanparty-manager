package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;

import java.util.List;

@Repository
public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Long> {

    List<AppUserToken> findByAppUser(AppUser appUser);

    AppUserToken findByToken(String token);
}
