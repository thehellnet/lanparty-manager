package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.MatchParent;

@Repository
public interface MatchParentRepository extends JpaRepository<MatchParent, Long> {
}
