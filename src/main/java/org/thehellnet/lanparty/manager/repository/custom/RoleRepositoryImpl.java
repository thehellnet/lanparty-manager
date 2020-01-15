package org.thehellnet.lanparty.manager.repository.custom;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.repository.RoleRepository;

@Repository
public class RoleRepositoryImpl implements RoleRepositoryCustom {

    private final RoleRepository roleRepository;

    @Lazy
    public RoleRepositoryImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByName(roleName.getValue());
    }
}
