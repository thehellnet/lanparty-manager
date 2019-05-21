package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;

import java.util.Set;

@Repository
public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Long> {

    AppUserToken findByToken(String token);
}
