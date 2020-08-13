package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Pane;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface PaneRepository extends JpaRepository<Pane, Long> {

    @Query("SELECT p.id " +
            "FROM Pane p " +
            "WHERE p.showcase = :showcase " +
            "ORDER BY p.displayOrder DESC, " +
            "  p.id")
    List<Long> findAllByShowcaseOrdered(@Param("showcase") Showcase showcase);

    Pane findByShowcaseAndId(Showcase showcase, Long id);

    Pane findByShowcaseAndMode(Showcase showcase, PaneMode mode);

    Pane findByShowcaseAndModeAndTournament(Showcase showcase, PaneMode mode, Tournament tournament);

    Pane findByShowcaseAndModeAndMatch(Showcase showcase, PaneMode mode, Match match);
}
