package org.thehellnet.lanparty.manager.repository.custom;

import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.persistence.Role;

public interface RoleRepositoryCustom {

    Role findByRoleName(RoleName roleName);
}
