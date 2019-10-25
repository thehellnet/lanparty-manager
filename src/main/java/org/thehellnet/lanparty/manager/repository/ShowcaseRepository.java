package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;

@Repository
public interface ShowcaseRepository extends JpaRepository<Showcase, Long> {

    Showcase findByTag(String tag);
}
