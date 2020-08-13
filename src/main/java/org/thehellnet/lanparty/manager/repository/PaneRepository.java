package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Pane;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface PaneRepository extends JpaRepository<Pane, Long> {

    List<Pane> findAllByShowcaseOrderByDisplayOrderDesc(Showcase showcase);

    Pane findByShowcaseAndId(Showcase showcase, Long id);

    Pane findByShowcaseAndMode(Showcase showcase, PaneMode mode);

    Pane findByShowcaseAndModeAndTournament(Showcase showcase, PaneMode mode, Tournament tournament);

    Pane findByShowcaseAndModeAndMatch(Showcase showcase, PaneMode mode, Match match);
}
