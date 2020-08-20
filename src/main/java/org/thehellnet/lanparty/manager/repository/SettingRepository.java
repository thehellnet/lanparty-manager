package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    Setting findByParam(String param);
}
