package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    Platform findByTag(String tag);
}
