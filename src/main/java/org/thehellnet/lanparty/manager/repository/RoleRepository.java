package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.repository.custom.RoleRepositoryCustom;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {

    Role findByName(String name);
}
