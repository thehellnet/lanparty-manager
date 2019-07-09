package org.thehellnet.lanparty.manager.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.utility.PasswordUtility;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
public class Initialization {

    private static final String GAME_Q3A = "q3a";
    private static final String GAME_Q3UT4 = "q3ut4";
    private static final String GAME_COD = "cod";
    private static final String GAME_COD2 = "cod2";
    private static final String GAME_COD4 = "cod4";
    private static final String GAME_CODWAW = "codwaw";
    private static final String GAME_LOL = "lol";

    private static final String GAMETYPE_DEATHMATCH = "Deathmatch";
    private static final String GAMETYPE_TEAM_DEATHMATCH = "Team Deathmatch";
    private static final String GAMETYPE_HEADQUARTERS = "Headquarters";
    private static final String GAMETYPE_SEARCH_AND_DESTROY = "Search and Destroy";
    private static final String GAMETYPE_BEHIND_ENEMY_LINES = "Behind enemy lines";
    private static final String GAMETYPE_RETRIEVAL = "Retrieval";
    private static final String GAMETYPE_CAPTURE_THE_FLAG = "Capture the flag";
    private static final String GAMETYPE_DOMINATION = "Domination";
    private static final String GAMETYPE_SABOTAGE = "Sabotage";
    private static final String GAMETYPE_BOMB_MODE = "Bomb mode";
    private static final String GAMETYPE_TEAM_SURVIVOR = "Team survivor";
    private static final String GAMETYPE_FOLLOW_THE_LEADER = "Follow the leader";
    private static final String GAMETYPE_CAPTURE_AND_HOLD = "Capture and hold";
    private static final String GAMETYPE_JUMP_TRAINING = "Jump training";
    private static final String GAMETYPE_FREEZE_TAG = "Freeze tag";
    private static final String GAMETYPE_LAST_MAN_STANDING = "Last man standing";
    private static final String GAMETYPE_TOTAL_WAR = "Total war";
    private static final String GAMETYPE_TOURNAMENT = "Tournament";
    private static final String GAMETYPE_SINGLE_PLAYER = "Single player";
    private static final String GAMETYPE_ONE_FLAG_CTF = "One flag CTF";
    private static final String GAMETYPE_OVERLOAD = "Overload";
    private static final String GAMETYPE_HARVESTER = "Harvester";

    private static final Logger logger = LoggerFactory.getLogger(Initialization.class);

    private boolean alreadyRun = false;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;
    private final GameGametypeRepository gameGametypeRepository;
    private final GameMapRepository gameMapRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public Initialization(ApplicationEventPublisher applicationEventPublisher, GameRepository gameRepository, GametypeRepository gametypeRepository, GameGametypeRepository gameGametypeRepository, GameMapRepository gameMapRepository, AppUserRepository appUserRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
        this.gameGametypeRepository = gameGametypeRepository;
        this.gameMapRepository = gameMapRepository;
        this.appUserRepository = appUserRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        if (alreadyRun) {
            return;
        }

        alreadyRun = true;

        logger.info("Initializing database data");

        checkGames();
        checkGametypes();
        checkGameGametypes();
        checkGameMaps();

        checkAppUsers();
        checkAppUserRoles();

        logger.info("Database data initialization complete");

        InitializedEvent initializedEvent = new InitializedEvent(this);
        applicationEventPublisher.publishEvent(initializedEvent);
    }

    private void checkGames() {
        logger.debug("Checking games");

        Map<String, String> gameMap = new HashMap<>();
        gameMap.put(GAME_Q3A, "Quake III Arena");
        gameMap.put(GAME_Q3UT4, "Urban Terror");
        gameMap.put(GAME_COD, "Call of Duty");
        gameMap.put(GAME_COD2, "Call of Duty 2");
        gameMap.put(GAME_COD4, "Call of Duty 4: Modern Warfare");
        gameMap.put(GAME_CODWAW, "Call of Duty: World at War");
        gameMap.put(GAME_LOL, "League of Legends");

        for (String tag : gameMap.keySet()) {
            Game game = gameRepository.findByTag(tag);
            if (game == null) {
                game = new Game(tag, gameMap.get(tag));
                gameRepository.save(game);
            }
        }
    }

    private void checkGametypes() {
        logger.debug("Checking gametypes");

        String[] gametypeNames = {
                GAMETYPE_DEATHMATCH,
                GAMETYPE_TEAM_DEATHMATCH,
                GAMETYPE_HEADQUARTERS,
                GAMETYPE_SEARCH_AND_DESTROY,
                GAMETYPE_BEHIND_ENEMY_LINES,
                GAMETYPE_RETRIEVAL,
                GAMETYPE_CAPTURE_THE_FLAG,
                GAMETYPE_DOMINATION,
                GAMETYPE_SABOTAGE,
                GAMETYPE_BOMB_MODE,
                GAMETYPE_TEAM_SURVIVOR,
                GAMETYPE_FOLLOW_THE_LEADER,
                GAMETYPE_CAPTURE_AND_HOLD,
                GAMETYPE_JUMP_TRAINING,
                GAMETYPE_FREEZE_TAG,
                GAMETYPE_LAST_MAN_STANDING,
                GAMETYPE_TOTAL_WAR,
                GAMETYPE_TOURNAMENT,
                GAMETYPE_SINGLE_PLAYER,
                GAMETYPE_ONE_FLAG_CTF,
                GAMETYPE_OVERLOAD,
                GAMETYPE_HARVESTER
        };

        for (String gametypeName : gametypeNames) {
            Gametype gametype = gametypeRepository.findByName(gametypeName);
            if (gametype == null) {
                gametype = new Gametype(gametypeName);
                gametypeRepository.save(gametype);
            }
        }
    }

    private void checkGameGametypes() {
        logger.debug("Checking game gametypes");

        Map<String, String> gametypeTags = new HashMap<>();

        gametypeTags.put(GAMETYPE_DEATHMATCH, "0");
        gametypeTags.put(GAMETYPE_TOURNAMENT, "1");
        gametypeTags.put(GAMETYPE_SINGLE_PLAYER, "2");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "3");
        gametypeTags.put(GAMETYPE_CAPTURE_THE_FLAG, "4");
        gametypeTags.put(GAMETYPE_ONE_FLAG_CTF, "5");
        gametypeTags.put(GAMETYPE_OVERLOAD, "6");
        gametypeTags.put(GAMETYPE_HARVESTER, "7");
        persistGameGametypes(gametypeTags, GAME_Q3A);

        gametypeTags.clear();
        gametypeTags.put(GAMETYPE_DEATHMATCH, "0");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "3");
        gametypeTags.put(GAMETYPE_TEAM_SURVIVOR, "4");
        gametypeTags.put(GAMETYPE_FOLLOW_THE_LEADER, "5");
        gametypeTags.put(GAMETYPE_CAPTURE_AND_HOLD, "6");
        gametypeTags.put(GAMETYPE_CAPTURE_THE_FLAG, "7");
        gametypeTags.put(GAMETYPE_BOMB_MODE, "8");
        gametypeTags.put(GAMETYPE_JUMP_TRAINING, "9");
        gametypeTags.put(GAMETYPE_FREEZE_TAG, "10");
        persistGameGametypes(gametypeTags, GAME_Q3UT4);

        gametypeTags.clear();
        gametypeTags.put(GAMETYPE_DEATHMATCH, "dm");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "tdm");
        gametypeTags.put(GAMETYPE_HEADQUARTERS, "hq");
        gametypeTags.put(GAMETYPE_SEARCH_AND_DESTROY, "sd");
        gametypeTags.put(GAMETYPE_BEHIND_ENEMY_LINES, "bel");
        gametypeTags.put(GAMETYPE_RETRIEVAL, "ret");
        persistGameGametypes(gametypeTags, GAME_COD);

        gametypeTags.clear();
        gametypeTags.put(GAMETYPE_DEATHMATCH, "dm");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "tdm");
        gametypeTags.put(GAMETYPE_HEADQUARTERS, "hq");
        gametypeTags.put(GAMETYPE_SEARCH_AND_DESTROY, "sd");
        gametypeTags.put(GAMETYPE_CAPTURE_THE_FLAG, "ctf");
        persistGameGametypes(gametypeTags, GAME_COD2);

        gametypeTags.clear();
        gametypeTags.put(GAMETYPE_DEATHMATCH, "dm");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "war");
        gametypeTags.put(GAMETYPE_HEADQUARTERS, "koth");
        gametypeTags.put(GAMETYPE_SEARCH_AND_DESTROY, "sd");
        gametypeTags.put(GAMETYPE_DOMINATION, "dom");
        gametypeTags.put(GAMETYPE_SABOTAGE, "sab");
        persistGameGametypes(gametypeTags, GAME_COD4);

        gametypeTags.clear();
        gametypeTags.put(GAMETYPE_DEATHMATCH, "dm");
        gametypeTags.put(GAMETYPE_TEAM_DEATHMATCH, "tdm");
        gametypeTags.put(GAMETYPE_HEADQUARTERS, "koth");
        gametypeTags.put(GAMETYPE_SEARCH_AND_DESTROY, "sd");
        gametypeTags.put(GAMETYPE_DOMINATION, "dom");
        gametypeTags.put(GAMETYPE_SABOTAGE, "sab");
        gametypeTags.put(GAMETYPE_CAPTURE_THE_FLAG, "ctf");
        gametypeTags.put(GAMETYPE_TOTAL_WAR, "twar");
        persistGameGametypes(gametypeTags, GAME_CODWAW);
    }

    private void checkGameMaps() {
        logger.debug("Checking game maps");

        Map<String, String> gameMaps = new HashMap<>();

        gameMaps.put("q3dm0", "Introduction");
        gameMaps.put("q3dm1", "Arena Gate");
        gameMaps.put("q3dm2", "House of Pain");
        gameMaps.put("q3dm3", "Arena of Death");
        gameMaps.put("q3dm4", "Place of Many Deaths");
        gameMaps.put("q3dm5", "The Forgotten Place");
        gameMaps.put("q3dm6", "The Camping Grounds");
        gameMaps.put("q3dm7", "Temple of Retribution");
        gameMaps.put("q3dm8", "Brimstone Abbey");
        gameMaps.put("q3dm9", "Hero's Keep");
        gameMaps.put("q3dm10", "The Nameless Place");
        gameMaps.put("q3dm11", "Deva Station");
        gameMaps.put("q3dm12", "The Dredwerkz");
        gameMaps.put("q3dm13", "Lost World");
        gameMaps.put("q3dm14", "Grim Dungeons");
        gameMaps.put("q3dm15", "Demon Keep");
        gameMaps.put("q3dm16", "Bouncy Map");
        gameMaps.put("q3dm17", "The Longest Yard");
        gameMaps.put("q3dm18", "Space Chamber");
        gameMaps.put("q3dm19", "Apocalypse Void");
        gameMaps.put("q3tourney1", "Power Station 0218");
        gameMaps.put("q3tourney2", "The Proving Grounds");
        gameMaps.put("q3tourney3", "Hell's Gate");
        gameMaps.put("q3tourney4", "Vertical Vengeance");
        gameMaps.put("q3tourney5", "Fatal Instinct");
        gameMaps.put("q3tourney6", "The Very End of You");
        gameMaps.put("q3ctf1", "Dueling Keeps");
        gameMaps.put("q3ctf2", "Troubled Waters");
        gameMaps.put("q3ctf3", "The Stronghold");
        gameMaps.put("q3ctf4", "Space CTF");
        persisitGameMap(gameMaps, GAME_Q3A);

        gameMaps.clear();
        gameMaps.put("ut4_abbey", "Abbey");
        gameMaps.put("ut4_abbeyctf", "Abbey CTF");
        gameMaps.put("ut4_algiers", "Algiers");
        gameMaps.put("ut4_ambush", "Ambush");
        gameMaps.put("ut4_austria", "Austria");
        gameMaps.put("ut4_bohemia", "Bohemia");
        gameMaps.put("ut4_casa", "Casa");
        gameMaps.put("ut4_cascade", "Cascade");
        gameMaps.put("ut4_commune", "Commune");
        gameMaps.put("ut4_company", "Company");
        gameMaps.put("ut4_crossing", "Crossing");
        gameMaps.put("ut4_docks", "Docks");
        gameMaps.put("ut4_dressingroom", "Dressing Room");
        gameMaps.put("ut4_eagle", "Eagle");
        gameMaps.put("ut4_elgin", "Elgin");
        gameMaps.put("ut4_ghosttown_rc4", "Ghost Town");
        gameMaps.put("ut4_harbortown", "Harbor Town");
        gameMaps.put("ut4_horror", "Horror");
        gameMaps.put("ut4_jumpents", "Jumpents");
        gameMaps.put("ut4_kingdom", "Kingdom");
        gameMaps.put("ut4_mandolin", "Mandolin");
        gameMaps.put("ut4_maya", "Maya");
        gameMaps.put("ut4_oildepot", "Oil Depot");
        gameMaps.put("ut4_prague", "Prague");
        gameMaps.put("ut4_raiders", "Raiders");
        gameMaps.put("ut4_ramelle", "Ramelle");
        gameMaps.put("ut4_ricochet", "Ricochet");
        gameMaps.put("ut4_riyadh", "Riyadh");
        gameMaps.put("ut4_sanc", "Sanctuary");
        gameMaps.put("ut4_snoppis", "Snoppis");
        gameMaps.put("ut4_suburbs", "Suburbs");
        gameMaps.put("ut4_subway", "Subway");
        gameMaps.put("ut4_swim", "Swim");
        gameMaps.put("ut4_thingley", "Thingley");
        gameMaps.put("ut4_tombs", "Tombs");
        gameMaps.put("ut4_toxic", "Toxic");
        gameMaps.put("ut4_tunis", "Tunis");
        gameMaps.put("ut4_turnpike", "Turnpike");
        gameMaps.put("ut4_uptown", "Uptown");
        persisitGameMap(gameMaps, GAME_Q3UT4);

        gameMaps.clear();
        gameMaps.put("mp_bocage", "Bocage");
        gameMaps.put("mp_brecourt", "Brecourt");
        gameMaps.put("mp_carentan", "Carentan");
        gameMaps.put("mp_chateau", "Chateau");
        gameMaps.put("mp_dawnville", "Dawnville");
        gameMaps.put("mp_depot", "Depot");
        gameMaps.put("mp_harbor", "Harbor");
        gameMaps.put("mp_hurtgen", "Hurtgen");
        gameMaps.put("mp_neuville", "Neuville");
        gameMaps.put("mp_pavlov", "Pavlov");
        gameMaps.put("mp_powcamp", "POW Camp");
        gameMaps.put("mp_railyard", "Railyard");
        gameMaps.put("mp_rocket", "Rocket");
        gameMaps.put("mp_ship", "Ship");
        gameMaps.put("mp_stalingrad", "Stalingrad");
        gameMaps.put("mp_tigertown", "Tigertown");
        persisitGameMap(gameMaps, GAME_COD);

        gameMaps.clear();
        gameMaps.put("mp_burgundy", "Burgundy");
        gameMaps.put("mp_caen", "Caen");
        gameMaps.put("mp_carentan", "Carentan");
        gameMaps.put("mp_brecourt", "Brecourt");
        gameMaps.put("mp_beltot", "Beltot");
        gameMaps.put("mp_dawnville", "Sainte-Mère-Eglise");
        gameMaps.put("mp_stalingrad", "Stalingrad");
        gameMaps.put("mp_moscow", "Moscow");
        gameMaps.put("mp_leningrad", "Leningrad");
        gameMaps.put("mp_breakout", "Villers - Bocage");
        gameMaps.put("mp_matmata", "Matmata");
        gameMaps.put("mp_toujane", "Toujane");
        gameMaps.put("mp_decoy", "El Alemein");
        gameMaps.put("mp_rhine", "Wallendar");
        persisitGameMap(gameMaps, GAME_COD2);

        gameMaps.clear();
        gameMaps.put("mp_ambush", "Ambush");
        gameMaps.put("mp_backlot", "Backlot");
        gameMaps.put("mp_bloc", "Bloc");
        gameMaps.put("mp_bog", "Bog");
        gameMaps.put("mp_broadcast", "Broadcast");
        gameMaps.put("mp_carentan", "Chinatown");
        gameMaps.put("mp_countdown", "Countdown");
        gameMaps.put("mp_crash", "Crash");
        gameMaps.put("mp_crash_snow", "Crash winter");
        gameMaps.put("mp_creek", "Creek");
        gameMaps.put("mp_crossfire", "Crossfire");
        gameMaps.put("mp_citystreets", "District");
        gameMaps.put("mp_farm", "Downpour");
        gameMaps.put("mp_killhouse", "Killhouse");
        gameMaps.put("mp_overgrown", "Overgrown");
        gameMaps.put("mp_pipeline", "Pipeline");
        gameMaps.put("mp_shipment", "Shipment");
        gameMaps.put("mp_showdown", "Showdown");
        gameMaps.put("mp_strike", "Strike");
        gameMaps.put("mp_vacant", "Vacant");
        gameMaps.put("mp_cargoship", "Wet Work");
        persisitGameMap(gameMaps, GAME_COD4);

        gameMaps.clear();
        gameMaps.put("mp_airfield", "Airfield");
        gameMaps.put("mp_asylum", "Asylum");
        gameMaps.put("mp_kwai", "Banzai");
        gameMaps.put("mp_drum", "Battery");
        gameMaps.put("mp_bgate", "Breach");
        gameMaps.put("mp_castle", "Castle");
        gameMaps.put("mp_shrine", "Cliffside");
        gameMaps.put("mp_stalingrad", "Corrosion");
        gameMaps.put("mp_courtyard", "Courtyard");
        gameMaps.put("mp_dome", "Dome");
        gameMaps.put("mp_downfall", "Downfall");
        gameMaps.put("mp_hangar", "Hangar");
        gameMaps.put("mp_kneedeep", "Knee Deep");
        gameMaps.put("mp_makin", "Makin");
        gameMaps.put("mp_makin_day", "Makin Day");
        gameMaps.put("mp_nachtfeuer", "Nightfire");
        gameMaps.put("mp_outskirts", "Outskirt");
        gameMaps.put("mp_vodka", "Revolution");
        gameMaps.put("mp_roundhouse", "Roundhouse");
        gameMaps.put("mp_seelow", "Seelow");
        gameMaps.put("mp_subway", "Station");
        gameMaps.put("mp_docks", "Sub Pens");
        gameMaps.put("mp_suburban", "Upheaval");
        persisitGameMap(gameMaps, GAME_CODWAW);
    }

    private void checkAppUsers() {
        logger.debug("Checking users");

        Map<String, String> userMap = new HashMap<>();
        userMap.put("admin", "admin");

        for (String userEmail : userMap.keySet()) {
            AppUser appUser = appUserRepository.findByEmail(userEmail);
            if (appUser == null) {
                String password = userMap.get(userEmail);
                String hashedPassword = PasswordUtility.hash(password);
                appUser = new AppUser(userEmail, hashedPassword);
                appUserRepository.save(appUser);
            }
        }
    }

    private void checkAppUserRoles() {
        logger.debug("Checking user roles assigment");

        Map<String, Role[]> userRoleMap = new HashMap<>();
        userRoleMap.put("admin", Role.values());

        for (String userEmail : userRoleMap.keySet()) {
            AppUser appUser = appUserRepository.findByEmail(userEmail);
            for (Role role : userRoleMap.get(userEmail)) {
                appUser.getAppUserRoles().add(role);
            }
            appUserRepository.save(appUser);
        }
    }

    private void persistGameGametypes(Map<String, String> gametypeTags, String gameTag) {
        Game game = gameRepository.findByTag(gameTag);

        for (String gametypeTag : gametypeTags.keySet()) {
            Gametype gametype = gametypeRepository.findByName(gametypeTag);
            if (gameGametypeRepository.findByGameAndGametype(game, gametype) == null) {
                gameGametypeRepository.save(new GameGametype(game, gametype, gametypeTags.get(gametypeTag)));
            }
        }
    }

    private void persisitGameMap(Map<String, String> gameMaps, String gameTag) {
        Game game = gameRepository.findByTag(gameTag);

        for (String mapTag : gameMaps.keySet()) {
            if (gameMapRepository.findByTagAndGame(mapTag, game) == null) {
                gameMapRepository.save(new GameMap(mapTag, gameMaps.get(mapTag), game, true));
            }
        }
    }
}
