package org.thehellnet.lanparty.manager.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.utility.PasswordUtility;

import java.util.List;

@Component
@Transactional
public class Initialization {

    private static final String PLATFORM_PC = "pc";
    private static final String PLATFORM_RPI = "rpi";
    private static final String PLATFORM_PS4 = "ps";
    private static final String PLATFORM_XBOX = "xbox";
    private static final String PLATFORM_SWITCH = "switch";
    private static final String PLATFORM_MOBILE = "mobile";
    private static final String PLATFORM_3DS = "3ds";

    private static final String GAME_Q3A = "q3a";
    private static final String GAME_Q3UT4 = "q3ut4";
    private static final String GAME_COD = "cod";
    private static final String GAME_COD2 = "cod2";
    private static final String GAME_COD4 = "cod4";
    private static final String GAME_CODWAW = "codwaw";
    private static final String GAME_LOL = "lol";
    private static final String GAME_RS = "rs";
    private static final String GAME_FN = "fn";
    private static final String GAME_FIFA20 = "fifa20";
    private static final String GAME_TEKKEN7 = "tekken7";
    private static final String GAME_HS = "hs";
    private static final String GAME_MARIOKART = "mariokart";
    private static final String GAME_SSBROS = "ssbros";
    private static final String GAME_POKEMON = "pokemon";
    private static final String GAME_JUSTDANCE = "justdance";

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
    private static final String GAMETYPE_1V1 = "1v1";
    private static final String GAMETYPE_5V5 = "5v5";

    private static final Logger logger = LoggerFactory.getLogger(Initialization.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RoleRepository roleRepository;
    private final PlatformRepository platformRepository;
    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;
    private final GameGametypeRepository gameGametypeRepository;
    private final GameMapRepository gameMapRepository;
    private final AppUserRepository appUserRepository;

    private boolean alreadyRun = false;

    @Autowired
    public Initialization(ApplicationEventPublisher applicationEventPublisher,
                          RoleRepository roleRepository,
                          PlatformRepository platformRepository,
                          GameRepository gameRepository,
                          GametypeRepository gametypeRepository,
                          GameGametypeRepository gameGametypeRepository,
                          GameMapRepository gameMapRepository,
                          AppUserRepository appUserRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.roleRepository = roleRepository;
        this.platformRepository = platformRepository;
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
        this.gameGametypeRepository = gameGametypeRepository;
        this.gameMapRepository = gameMapRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        if (alreadyRun) {
            return;
        }

        alreadyRun = true;

        logger.info("Initializing database data");

        checkRoles();
        checkAppUsers();

        checkPlatforms();
        checkGames();
        checkGametypes();
        checkGameGametypes();
        checkGameMaps();

        logger.info("Database data initialization complete");

        InitializedEvent initializedEvent = new InitializedEvent(this);
        applicationEventPublisher.publishEvent(initializedEvent);
    }

    private void checkRoles() {
        logger.info("Checking roles");

        for (RoleName roleName : RoleName.values()) {
            persistRole(roleName);
        }
    }

    private void checkAppUsers() {
        logger.info("Checking users");

        persistAppUser("admin", "admin");
    }

    private void checkPlatforms() {
        logger.info("Checking platforms");

        persistPlatform(PLATFORM_PC, "PC Windows");
        persistPlatform(PLATFORM_RPI, "Raspberry Pi");
        persistPlatform(PLATFORM_PS4, "PlayStation 4");
        persistPlatform(PLATFORM_XBOX, "Xbox");
        persistPlatform(PLATFORM_SWITCH, "Nintendo Switch");
        persistPlatform(PLATFORM_MOBILE, "Personal Mobile device");
        persistPlatform(PLATFORM_3DS, "Nientendo 2DS/3DS");
    }

    private void checkGames() {
        logger.info("Checking games");

        persistGame(GAME_Q3A, "Quake III Arena", PLATFORM_PC);
        persistGame(GAME_Q3UT4, "Urban Terror", PLATFORM_PC);
        persistGame(GAME_COD, "Call of Duty", PLATFORM_PC);
        persistGame(GAME_COD2, "Call of Duty 2", PLATFORM_PC);
        persistGame(GAME_COD4, "Call of Duty 4: Modern Warfare", PLATFORM_PC);
        persistGame(GAME_CODWAW, "Call of Duty: World at War", PLATFORM_PC);
        persistGame(GAME_LOL, "League of Legends", PLATFORM_PC);
        persistGame(GAME_RS, "Tom Clancy's Rainbow Six Siege", PLATFORM_PS4);
        persistGame(GAME_FN, "Fortnite", PLATFORM_PC);
        persistGame(GAME_FIFA20, "FIFA 20", PLATFORM_PS4);
        persistGame(GAME_TEKKEN7, "Tekken 7", PLATFORM_PS4);
        persistGame(GAME_HS, "Hearthstone", PLATFORM_MOBILE);
        persistGame(GAME_MARIOKART, "Mario Kart 8 Deluxe", PLATFORM_SWITCH);
        persistGame(GAME_SSBROS, "Super Smash Bros", PLATFORM_SWITCH);
        persistGame(GAME_POKEMON, "Pokémon VGC Premier Challenge", PLATFORM_3DS);
        persistGame(GAME_JUSTDANCE, "Just Dance", PLATFORM_SWITCH);
    }

    private void checkGametypes() {
        logger.info("Checking gametypes");

        persistGametype(GAMETYPE_DEATHMATCH);
        persistGametype(GAMETYPE_TEAM_DEATHMATCH);
        persistGametype(GAMETYPE_HEADQUARTERS);
        persistGametype(GAMETYPE_SEARCH_AND_DESTROY);
        persistGametype(GAMETYPE_BEHIND_ENEMY_LINES);
        persistGametype(GAMETYPE_RETRIEVAL);
        persistGametype(GAMETYPE_CAPTURE_THE_FLAG);
        persistGametype(GAMETYPE_DOMINATION);
        persistGametype(GAMETYPE_SABOTAGE);
        persistGametype(GAMETYPE_BOMB_MODE);
        persistGametype(GAMETYPE_TEAM_SURVIVOR);
        persistGametype(GAMETYPE_FOLLOW_THE_LEADER);
        persistGametype(GAMETYPE_CAPTURE_AND_HOLD);
        persistGametype(GAMETYPE_JUMP_TRAINING);
        persistGametype(GAMETYPE_FREEZE_TAG);
        persistGametype(GAMETYPE_LAST_MAN_STANDING);
        persistGametype(GAMETYPE_TOTAL_WAR);
        persistGametype(GAMETYPE_TOURNAMENT);
        persistGametype(GAMETYPE_SINGLE_PLAYER);
        persistGametype(GAMETYPE_ONE_FLAG_CTF);
        persistGametype(GAMETYPE_OVERLOAD);
        persistGametype(GAMETYPE_HARVESTER);
        persistGametype(GAMETYPE_1V1);
        persistGametype(GAMETYPE_5V5);
    }

    private void checkGameGametypes() {
        logger.info("Checking game gametypes");

        persistGameGametype(GAME_Q3A, GAMETYPE_DEATHMATCH, "0");
        persistGameGametype(GAME_Q3A, GAMETYPE_TOURNAMENT, "1");
        persistGameGametype(GAME_Q3A, GAMETYPE_SINGLE_PLAYER, "2");
        persistGameGametype(GAME_Q3A, GAMETYPE_TEAM_DEATHMATCH, "3");
        persistGameGametype(GAME_Q3A, GAMETYPE_CAPTURE_THE_FLAG, "4");
        persistGameGametype(GAME_Q3A, GAMETYPE_ONE_FLAG_CTF, "5");
        persistGameGametype(GAME_Q3A, GAMETYPE_OVERLOAD, "6");
        persistGameGametype(GAME_Q3A, GAMETYPE_HARVESTER, "7");

        persistGameGametype(GAME_Q3UT4, GAMETYPE_DEATHMATCH, "0");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_TEAM_DEATHMATCH, "3");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_TEAM_SURVIVOR, "4");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_FOLLOW_THE_LEADER, "5");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_CAPTURE_AND_HOLD, "6");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_CAPTURE_THE_FLAG, "7");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_BOMB_MODE, "8");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_JUMP_TRAINING, "9");
        persistGameGametype(GAME_Q3UT4, GAMETYPE_FREEZE_TAG, "10");

        persistGameGametype(GAME_COD, GAMETYPE_DEATHMATCH, "dm");
        persistGameGametype(GAME_COD, GAMETYPE_TEAM_DEATHMATCH, "tdm");
        persistGameGametype(GAME_COD, GAMETYPE_HEADQUARTERS, "hq");
        persistGameGametype(GAME_COD, GAMETYPE_SEARCH_AND_DESTROY, "sd");
        persistGameGametype(GAME_COD, GAMETYPE_BEHIND_ENEMY_LINES, "bel");
        persistGameGametype(GAME_COD, GAMETYPE_RETRIEVAL, "ret");

        persistGameGametype(GAME_COD2, GAMETYPE_DEATHMATCH, "dm");
        persistGameGametype(GAME_COD2, GAMETYPE_TEAM_DEATHMATCH, "tdm");
        persistGameGametype(GAME_COD2, GAMETYPE_HEADQUARTERS, "hq");
        persistGameGametype(GAME_COD2, GAMETYPE_SEARCH_AND_DESTROY, "sd");
        persistGameGametype(GAME_COD2, GAMETYPE_CAPTURE_THE_FLAG, "ctf");

        persistGameGametype(GAME_COD4, GAMETYPE_DEATHMATCH, "dm");
        persistGameGametype(GAME_COD4, GAMETYPE_TEAM_DEATHMATCH, "war");
        persistGameGametype(GAME_COD4, GAMETYPE_HEADQUARTERS, "koth");
        persistGameGametype(GAME_COD4, GAMETYPE_SEARCH_AND_DESTROY, "sd");
        persistGameGametype(GAME_COD4, GAMETYPE_DOMINATION, "dom");
        persistGameGametype(GAME_COD4, GAMETYPE_SABOTAGE, "sab");

        persistGameGametype(GAME_CODWAW, GAMETYPE_DEATHMATCH, "dm");
        persistGameGametype(GAME_CODWAW, GAMETYPE_TEAM_DEATHMATCH, "tdm");
        persistGameGametype(GAME_CODWAW, GAMETYPE_HEADQUARTERS, "koth");
        persistGameGametype(GAME_CODWAW, GAMETYPE_SEARCH_AND_DESTROY, "sd");
        persistGameGametype(GAME_CODWAW, GAMETYPE_DOMINATION, "dom");
        persistGameGametype(GAME_CODWAW, GAMETYPE_SABOTAGE, "sab");
        persistGameGametype(GAME_CODWAW, GAMETYPE_CAPTURE_THE_FLAG, "ctf");
        persistGameGametype(GAME_CODWAW, GAMETYPE_TOTAL_WAR, "twar");

        persistGameGametype(GAME_LOL, GAMETYPE_1V1, GAMETYPE_1V1);
        persistGameGametype(GAME_LOL, GAMETYPE_5V5, GAMETYPE_5V5);
    }

    private void checkGameMaps() {
        logger.info("Checking game maps");

        persistGameMap(GAME_Q3A, "q3dm0", "Introduction", true);
        persistGameMap(GAME_Q3A, "q3dm1", "Arena Gate", true);
        persistGameMap(GAME_Q3A, "q3dm2", "House of Pain", true);
        persistGameMap(GAME_Q3A, "q3dm3", "Arena of Death", true);
        persistGameMap(GAME_Q3A, "q3dm4", "Place of Many Deaths", true);
        persistGameMap(GAME_Q3A, "q3dm5", "The Forgotten Place", true);
        persistGameMap(GAME_Q3A, "q3dm6", "The Camping Grounds", true);
        persistGameMap(GAME_Q3A, "q3dm7", "Temple of Retribution", true);
        persistGameMap(GAME_Q3A, "q3dm8", "Brimstone Abbey", true);
        persistGameMap(GAME_Q3A, "q3dm9", "Hero's Keep", true);
        persistGameMap(GAME_Q3A, "q3dm10", "The Nameless Place", true);
        persistGameMap(GAME_Q3A, "q3dm11", "Deva Station", true);
        persistGameMap(GAME_Q3A, "q3dm12", "The Dredwerkz", true);
        persistGameMap(GAME_Q3A, "q3dm13", "Lost World", true);
        persistGameMap(GAME_Q3A, "q3dm14", "Grim Dungeons", true);
        persistGameMap(GAME_Q3A, "q3dm15", "Demon Keep", true);
        persistGameMap(GAME_Q3A, "q3dm16", "Bouncy Map", true);
        persistGameMap(GAME_Q3A, "q3dm17", "The Longest Yard", true);
        persistGameMap(GAME_Q3A, "q3dm18", "Space Chamber", true);
        persistGameMap(GAME_Q3A, "q3dm19", "Apocalypse Void", true);
        persistGameMap(GAME_Q3A, "q3tourney1", "Power Station 0218", true);
        persistGameMap(GAME_Q3A, "q3tourney2", "The Proving Grounds", true);
        persistGameMap(GAME_Q3A, "q3tourney3", "Hell's Gate", true);
        persistGameMap(GAME_Q3A, "q3tourney4", "Vertical Vengeance", true);
        persistGameMap(GAME_Q3A, "q3tourney5", "Fatal Instinct", true);
        persistGameMap(GAME_Q3A, "q3tourney6", "The Very End of You", true);
        persistGameMap(GAME_Q3A, "q3ctf1", "Dueling Keeps", true);
        persistGameMap(GAME_Q3A, "q3ctf2", "Troubled Waters", true);
        persistGameMap(GAME_Q3A, "q3ctf3", "The Stronghold", true);
        persistGameMap(GAME_Q3A, "q3ctf4", "Space CTF", true);

        persistGameMap(GAME_Q3UT4, "ut4_abbey", "Abbey", true);
        persistGameMap(GAME_Q3UT4, "ut4_abbeyctf", "Abbey CTF", true);
        persistGameMap(GAME_Q3UT4, "ut4_algiers", "Algiers", true);
        persistGameMap(GAME_Q3UT4, "ut4_ambush", "Ambush", true);
        persistGameMap(GAME_Q3UT4, "ut4_austria", "Austria", true);
        persistGameMap(GAME_Q3UT4, "ut4_bohemia", "Bohemia", true);
        persistGameMap(GAME_Q3UT4, "ut4_casa", "Casa", true);
        persistGameMap(GAME_Q3UT4, "ut4_cascade", "Cascade", true);
        persistGameMap(GAME_Q3UT4, "ut4_commune", "Commune", true);
        persistGameMap(GAME_Q3UT4, "ut4_company", "Company", true);
        persistGameMap(GAME_Q3UT4, "ut4_crossing", "Crossing", true);
        persistGameMap(GAME_Q3UT4, "ut4_docks", "Docks", true);
        persistGameMap(GAME_Q3UT4, "ut4_dressingroom", "Dressing Room", true);
        persistGameMap(GAME_Q3UT4, "ut4_eagle", "Eagle", true);
        persistGameMap(GAME_Q3UT4, "ut4_elgin", "Elgin", true);
        persistGameMap(GAME_Q3UT4, "ut4_ghosttown_rc4", "Ghost Town", true);
        persistGameMap(GAME_Q3UT4, "ut4_harbortown", "Harbor Town", true);
        persistGameMap(GAME_Q3UT4, "ut4_horror", "Horror", true);
        persistGameMap(GAME_Q3UT4, "ut4_jumpents", "Jumpents", true);
        persistGameMap(GAME_Q3UT4, "ut4_kingdom", "Kingdom", true);
        persistGameMap(GAME_Q3UT4, "ut4_mandolin", "Mandolin", true);
        persistGameMap(GAME_Q3UT4, "ut4_maya", "Maya", true);
        persistGameMap(GAME_Q3UT4, "ut4_oildepot", "Oil Depot", true);
        persistGameMap(GAME_Q3UT4, "ut4_prague", "Prague", true);
        persistGameMap(GAME_Q3UT4, "ut4_raiders", "Raiders", true);
        persistGameMap(GAME_Q3UT4, "ut4_ramelle", "Ramelle", true);
        persistGameMap(GAME_Q3UT4, "ut4_ricochet", "Ricochet", true);
        persistGameMap(GAME_Q3UT4, "ut4_riyadh", "Riyadh", true);
        persistGameMap(GAME_Q3UT4, "ut4_sanc", "Sanctuary", true);
        persistGameMap(GAME_Q3UT4, "ut4_snoppis", "Snoppis", true);
        persistGameMap(GAME_Q3UT4, "ut4_suburbs", "Suburbs", true);
        persistGameMap(GAME_Q3UT4, "ut4_subway", "Subway", true);
        persistGameMap(GAME_Q3UT4, "ut4_swim", "Swim", true);
        persistGameMap(GAME_Q3UT4, "ut4_thingley", "Thingley", true);
        persistGameMap(GAME_Q3UT4, "ut4_tombs", "Tombs", true);
        persistGameMap(GAME_Q3UT4, "ut4_toxic", "Toxic", true);
        persistGameMap(GAME_Q3UT4, "ut4_tunis", "Tunis", true);
        persistGameMap(GAME_Q3UT4, "ut4_turnpike", "Turnpike", true);
        persistGameMap(GAME_Q3UT4, "ut4_uptown", "Uptown", true);

        persistGameMap(GAME_COD, "mp_bocage", "Bocage", true);
        persistGameMap(GAME_COD, "mp_brecourt", "Brecourt", true);
        persistGameMap(GAME_COD, "mp_carentan", "Carentan", true);
        persistGameMap(GAME_COD, "mp_chateau", "Chateau", true);
        persistGameMap(GAME_COD, "mp_dawnville", "Dawnville", true);
        persistGameMap(GAME_COD, "mp_depot", "Depot", true);
        persistGameMap(GAME_COD, "mp_harbor", "Harbor", true);
        persistGameMap(GAME_COD, "mp_hurtgen", "Hurtgen", true);
        persistGameMap(GAME_COD, "mp_neuville", "Neuville", true);
        persistGameMap(GAME_COD, "mp_pavlov", "Pavlov", true);
        persistGameMap(GAME_COD, "mp_powcamp", "POW Camp", true);
        persistGameMap(GAME_COD, "mp_railyard", "Railyard", true);
        persistGameMap(GAME_COD, "mp_rocket", "Rocket", true);
        persistGameMap(GAME_COD, "mp_ship", "Ship", true);
        persistGameMap(GAME_COD, "mp_stalingrad", "Stalingrad", true);
        persistGameMap(GAME_COD, "mp_tigertown", "Tigertown", true);

        persistGameMap(GAME_COD2, "mp_breakout", "Villers-Bocage", true);
        persistGameMap(GAME_COD2, "mp_brecourt", "Brecourt", true);
        persistGameMap(GAME_COD2, "mp_burgundy", "Burgundy", true);
        persistGameMap(GAME_COD2, "mp_carentan", "Carentan", true);
        persistGameMap(GAME_COD2, "mp_dawnville", "Sainte-Mère-Eglise", true);
        persistGameMap(GAME_COD2, "mp_decoy", "El Alemein", true);
        persistGameMap(GAME_COD2, "mp_farmhouse", "Beltot", true);
        persistGameMap(GAME_COD2, "mp_harbor", "Rostov", true);
        persistGameMap(GAME_COD2, "mp_leningrad", "Leningrad", true);
        persistGameMap(GAME_COD2, "mp_matmata", "Matmata", true);
        persistGameMap(GAME_COD2, "mp_moscow", "Moscow", true);
        persistGameMap(GAME_COD2, "mp_rhine", "Wallendar", true);
        persistGameMap(GAME_COD2, "mp_railyard", "Stalingrad", true);
        persistGameMap(GAME_COD2, "mp_toujane", "Toujane", true);
        persistGameMap(GAME_COD2, "mp_trainstation", "Caen", true);

        persistGameMap(GAME_COD4, "mp_ambush", "Ambush", true);
        persistGameMap(GAME_COD4, "mp_backlot", "Backlot", true);
        persistGameMap(GAME_COD4, "mp_bloc", "Bloc", true);
        persistGameMap(GAME_COD4, "mp_bog", "Bog", true);
        persistGameMap(GAME_COD4, "mp_broadcast", "Broadcast", true);
        persistGameMap(GAME_COD4, "mp_carentan", "Chinatown", true);
        persistGameMap(GAME_COD4, "mp_countdown", "Countdown", true);
        persistGameMap(GAME_COD4, "mp_crash", "Crash", true);
        persistGameMap(GAME_COD4, "mp_crash_snow", "Crash winter", true);
        persistGameMap(GAME_COD4, "mp_creek", "Creek", true);
        persistGameMap(GAME_COD4, "mp_crossfire", "Crossfire", true);
        persistGameMap(GAME_COD4, "mp_citystreets", "District", true);
        persistGameMap(GAME_COD4, "mp_farm", "Downpour", true);
        persistGameMap(GAME_COD4, "mp_killhouse", "Killhouse", true);
        persistGameMap(GAME_COD4, "mp_overgrown", "Overgrown", true);
        persistGameMap(GAME_COD4, "mp_pipeline", "Pipeline", true);
        persistGameMap(GAME_COD4, "mp_shipment", "Shipment", true);
        persistGameMap(GAME_COD4, "mp_showdown", "Showdown", true);
        persistGameMap(GAME_COD4, "mp_strike", "Strike", true);
        persistGameMap(GAME_COD4, "mp_vacant", "Vacant", true);
        persistGameMap(GAME_COD4, "mp_cargoship", "Wet Work", true);

        persistGameMap(GAME_CODWAW, "mp_airfield", "Airfield", true);
        persistGameMap(GAME_CODWAW, "mp_asylum", "Asylum", true);
        persistGameMap(GAME_CODWAW, "mp_kwai", "Banzai", true);
        persistGameMap(GAME_CODWAW, "mp_drum", "Battery", true);
        persistGameMap(GAME_CODWAW, "mp_bgate", "Breach", true);
        persistGameMap(GAME_CODWAW, "mp_castle", "Castle", true);
        persistGameMap(GAME_CODWAW, "mp_shrine", "Cliffside", true);
        persistGameMap(GAME_CODWAW, "mp_stalingrad", "Corrosion", true);
        persistGameMap(GAME_CODWAW, "mp_courtyard", "Courtyard", true);
        persistGameMap(GAME_CODWAW, "mp_dome", "Dome", true);
        persistGameMap(GAME_CODWAW, "mp_downfall", "Downfall", true);
        persistGameMap(GAME_CODWAW, "mp_hangar", "Hangar", true);
        persistGameMap(GAME_CODWAW, "mp_kneedeep", "Knee Deep", true);
        persistGameMap(GAME_CODWAW, "mp_makin", "Makin", true);
        persistGameMap(GAME_CODWAW, "mp_makin_day", "Makin Day", true);
        persistGameMap(GAME_CODWAW, "mp_nachtfeuer", "Nightfire", true);
        persistGameMap(GAME_CODWAW, "mp_outskirts", "Outskirt", true);
        persistGameMap(GAME_CODWAW, "mp_vodka", "Revolution", true);
        persistGameMap(GAME_CODWAW, "mp_roundhouse", "Roundhouse", true);
        persistGameMap(GAME_CODWAW, "mp_seelow", "Seelow", true);
        persistGameMap(GAME_CODWAW, "mp_subway", "Station", true);
        persistGameMap(GAME_CODWAW, "mp_docks", "Sub Pens", true);
        persistGameMap(GAME_CODWAW, "mp_suburban", "Upheaval", true);
    }

    private void persistRole(RoleName roleName) {
        logger.debug("Persist Role '{}'", roleName);

        Role role = roleRepository.findByName(roleName.getValue());
        if (role == null) {
            role = new Role(roleName.getValue());
        }
        roleRepository.save(role);
    }

    private void persistPlatform(String tag, String name) {
        logger.debug("Persist Platform '{}' '{}'", tag, name);

        Platform platform = platformRepository.findByTag(tag);
        if (platform == null) {
            platform = new Platform(tag);
        }
        platform.setName(name);
        platformRepository.save(platform);
    }

    private void persistAppUser(String email, String password) {
        logger.debug("Persist AppUser '{}'", email);

        List<Role> roles = roleRepository.findAll();

        AppUser appUser = appUserRepository.findByEnabledTrueAndEmail(email);
        if (appUser == null) {
            appUser = new AppUser(email);
            String hashedPassword = PasswordUtility.newInstance().hash(password);
            appUser.setPassword(hashedPassword);
        }
        appUser.setEnabled(true);
        appUser.setRoles(roles);
        appUserRepository.save(appUser);
    }

    private void persistGame(String tag, String name, String platformTag) {
        logger.debug("Persist Game '{}' '{}' '{}'", tag, name, platformTag);

        Platform platform = platformRepository.findByTag(platformTag);
        Game game = gameRepository.findByTag(tag);
        if (game == null) {
            game = new Game(tag);
        }
        game.setName(name);
        game.setPlatform(platform);
        gameRepository.save(game);
    }

    private void persistGametype(String name) {
        logger.debug("Persist Gametype '{}'", name);

        Gametype gametype = gametypeRepository.findByName(name);
        if (gametype == null) {
            gametype = new Gametype(name);
        }
        gametypeRepository.save(gametype);
    }

    private void persistGameGametype(String gameTag, String gametypeTag, String tag) {
        logger.debug("Persist GameGametype '{}' '{}' '{}'", gameTag, gametypeTag, tag);

        Game game = gameRepository.findByTag(gameTag);
        Gametype gametype = gametypeRepository.findByName(gametypeTag);
        GameGametype gameGametype = gameGametypeRepository.findByGameAndGametype(game, gametype);
        if (gameGametype == null) {
            gameGametype = new GameGametype(game, gametype);
        }
        gameGametype.setTag(tag);
        gameGametypeRepository.save(gameGametype);
    }

    private void persistGameMap(String gameTag, String tag, String name, boolean stock) {
        logger.debug("Persist GameMap '{}' '{}' '{}' '{}'", gameTag, tag, name, stock);

        Game game = gameRepository.findByTag(gameTag);
        GameMap gameMap = gameMapRepository.findByTagAndGame(tag, game);
        if (gameMap == null) {
            gameMap = new GameMap(tag);
        }
        gameMap.setName(name);
        gameMap.setGame(game);
        gameMap.setStock(stock);
        gameMapRepository.save(gameMap);
    }
}
