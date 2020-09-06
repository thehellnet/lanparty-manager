package org.thehellnet.lanparty.manager.service;


import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.thehellnet.lanparty.manager.exception.NotFoundException;
import org.thehellnet.lanparty.manager.job.ShowcaseNextPaneJob;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.model.protocol.Action;
import org.thehellnet.lanparty.manager.model.protocol.Command;
import org.thehellnet.lanparty.manager.model.protocol.CommandSerializer;
import org.thehellnet.lanparty.manager.repository.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class ShowcaseService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseService.class);

    private final Object SYNC = new Object();

    private final ShowcaseRepository showcaseRepository;
    private final PaneRepository paneRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    private final Scheduler scheduler;

    private final Map<Long, WebSocketSession> sessions = new HashMap<>();
    private final Map<Long, Long> currentPanes = new HashMap<>();

    @Autowired
    public ShowcaseService(SeatRepository seatRepository,
                           PlayerRepository playerRepository,
                           AppUserRepository appUserRepository,
                           ShowcaseRepository showcaseRepository,
                           PaneRepository paneRepository,
                           MatchRepository matchRepository,
                           TeamRepository teamRepository, Scheduler scheduler) {
        super(seatRepository, playerRepository, appUserRepository);
        this.showcaseRepository = showcaseRepository;
        this.paneRepository = paneRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.scheduler = scheduler;
    }

    @Transactional(readOnly = true)
    public Showcase findByTag(String tag) {
        Showcase showcase = showcaseRepository.findByTag(tag);
        if (showcase == null) {
            throw new NotFoundException();
        }
        return showcase;
    }

    @Transactional
    public void addShowcase(String tag, WebSocketSession webSocketSession) {
        synchronized (SYNC) {
            String ipAddress = Objects.requireNonNull(webSocketSession.getRemoteAddress()).getAddress().getHostAddress();

            Showcase showcase = showcaseRepository.findByTag(tag);
            if (showcase == null) {
                showcase = new Showcase(tag);
            }

            showcase.setConnected(true);
            showcase.setLastAddress(ipAddress);
            showcase.updateLastContact();
            showcase = showcaseRepository.save(showcase);

            sessions.put(showcase.getId(), webSocketSession);

            scheduleNextCallNow(showcase);

            logger.info("Showcase {} added from {}", tag, ipAddress);
        }
    }

    @Transactional
    public void removeShowcase(String tag) {
        synchronized (SYNC) {
            Showcase showcase = findByTag(tag);
            showcase.setConnected(false);
            showcase = showcaseRepository.save(showcase);

            sessions.remove(showcase.getId());
            currentPanes.remove(showcase.getId());

            deleteJob(showcase);

            logger.info("Showcase {} removed", tag);
        }
    }

    @Transactional
    public void showNextPane(Showcase showcase) {
        logger.info("Displaying next pane for showcase: {}", showcase);

        showcase = showcaseRepository.findById(showcase.getId()).orElseThrow();
        if (!sessions.containsKey(showcase.getId())) {
            return;
        }

        List<Long> paneIds = paneRepository.findAllByShowcaseOrdered(showcase);

        if (paneIds.isEmpty()) {
            return;
        }

        Long newPaneId;

        if (!currentPanes.containsKey(showcase.getId())) {
            newPaneId = paneIds.get(0);
        } else {
            Long paneId = currentPanes.get(showcase.getId());
            int currentPaneIndex = paneIds.indexOf(paneId);
            int nextPaneIndex = currentPaneIndex + 1;
            if (nextPaneIndex >= paneIds.size()) {
                newPaneId = paneIds.get(0);
            } else {
                newPaneId = paneIds.get(nextPaneIndex);
            }
        }

        Pane pane = paneRepository.findById(newPaneId).orElseThrow();
        logger.debug("Next pane: {} - Duration: {} seconds", pane, pane.getDuration());

        showPane(showcase, pane);
        scheduleNextCall(showcase, pane.getDuration());
    }

    @Transactional
    public void showPane(Showcase showcase, Pane pane) {
        showcase = showcaseRepository.findById(showcase.getId()).orElseThrow();
        pane = paneRepository.findById(pane.getId()).orElseThrow();

        if (!sessions.containsKey(showcase.getId())) {
            return;
        }

        Command command = generateDisplayPaneCommand(pane);
        String commandText = CommandSerializer.serialize(command);

        WebSocketSession webSocketSession = sessions.get(showcase.getId());
        TextMessage message = new TextMessage(commandText);
        try {
            webSocketSession.sendMessage(message);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        currentPanes.put(showcase.getId(), pane.getId());
    }

    private void scheduleNextCallNow(Showcase showcase) {
        scheduleNextCall(showcase, 0);
    }

    private void scheduleNextCall(Showcase showcase, int duration) {
        logger.debug("Scheduling next call for showcase {} after {} seconds", showcase, duration);
        LocalDateTime executionDateTime = LocalDateTime.now().plusSeconds(duration);
        scheduleJob(showcase, executionDateTime);
    }

    private void deleteJob(Showcase showcase) {
        logger.info("Deleting Job for {}", showcase);

        JobKey jobKey = prepareJobKey(showcase);

        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void scheduleJob(Showcase showcase, LocalDateTime executionDateTime) {
        logger.info("Scheduling Job for {}", showcase);

        JobKey jobKey = prepareJobKey(showcase);
        JobDetail jobDetail = prepareJobDetail(showcase, jobKey);
        Trigger trigger = prepareTrigger(executionDateTime, jobDetail);

        Set<Trigger> triggerSet = new HashSet<>();
        triggerSet.add(trigger);

        try {
            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Command generateDisplayPaneCommand(Pane pane) {
        JSONObject args = new JSONObject();

        PaneMode paneMode = pane.getMode();
        args.put("mode", paneMode);
        args.put("preScroll", 500);
        args.put("scrollDuration", 8000);

        if (paneMode == PaneMode.MATCHES) {
            Tournament tournament = pane.getTournament();
            List<Match> matches = matchRepository.findAllByTournament(tournament);

            JSONArray matchesData = new JSONArray();
            for (Match match : matches) {
                JSONObject matchData = prepareMatchData(match);
                matchesData.put(matchData);
            }
            args.put("matches", matchesData);

            Game game = tournament.getGame();
            args.put("gameTag", game.getTag());

            args.put("title", "Matches in Tournament");
        } else if (paneMode == PaneMode.SCORES) {
            Tournament tournament = pane.getTournament();
            List<Team> teams = teamRepository.findAllByTournament(tournament);

            JSONArray teamsData = new JSONArray();
            for (Team team : teams) {
                JSONObject teamData = prepareTeamData(team);
                teamsData.put(teamData);
            }
            args.put("teams", teamsData);

            Game game = tournament.getGame();
            args.put("gameTag", game.getTag());

            args.put("title", "Teams scores");
        }

        Command command = new Command(Action.DISPLAY_PANE);
        command.setArgs(args);
        return command;
    }

    private static JSONObject prepareMatchData(Match match) {
        JSONObject matchData = new JSONObject();
        matchData.put("id", match.getId());
        matchData.put("name", match.getName());
        matchData.put("status", match.getStatus());
        matchData.put("scheduledStartTs", match.getScheduledStartTs() != null ? match.getScheduledStartTs() : JSONObject.NULL);
        matchData.put("scheduledEndTs", match.getScheduledEndTs() != null ? match.getScheduledEndTs() : JSONObject.NULL);
        matchData.put("startTs", match.getStartTs() != null ? match.getStartTs() : JSONObject.NULL);
        matchData.put("endTs", match.getEndTs() != null ? match.getEndTs() : JSONObject.NULL);
        matchData.put("playOrder", match.getPlayOrder());
        matchData.put("level", match.getLevel());
        matchData.put("gameMap", match.getGameMap() != null ? match.getGameMap().getName() : JSONObject.NULL);
        matchData.put("gametype", match.getGametype() != null ? match.getGametype().getName() : JSONObject.NULL);
        matchData.put("localTeam", match.getLocalTeam() != null ? match.getLocalTeam().getName() : JSONObject.NULL);
        matchData.put("guestTeam", match.getGuestTeam() != null ? match.getGuestTeam().getName() : JSONObject.NULL);
        return matchData;
    }

    private static JSONObject prepareTeamData(Team team) {
        JSONObject teamData = new JSONObject();
        teamData.put("id", team.getId());
        teamData.put("name", team.getName());
        return teamData;
    }

    private static Trigger prepareTrigger(LocalDateTime executionDateTime, JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .startAt(Date.from(executionDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .forJob(jobDetail)
                .build();
    }

    private static JobDetail prepareJobDetail(Showcase showcase, JobKey jobKey) {
        return JobBuilder.newJob()
                .ofType(ShowcaseNextPaneJob.class)
                .withIdentity(jobKey)
                .usingJobData("showcaseId", showcase.getId())
                .build();
    }

    private static JobKey prepareJobKey(Showcase showcase) {
        return JobKey.jobKey(
                String.format("showcase-%d", showcase.getId()),
                "showcaseNextCall"
        );
    }
}
