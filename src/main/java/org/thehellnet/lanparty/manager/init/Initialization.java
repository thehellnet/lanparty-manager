package org.thehellnet.lanparty.manager.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.constant.SettingConstant;
import org.thehellnet.lanparty.manager.model.constant.FileHash;
import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.utility.PasswordUtility;

import java.util.List;

import static org.thehellnet.lanparty.manager.model.constant.Tags.*;

@Component
@Transactional
public class Initialization {

    private static final Logger logger = LoggerFactory.getLogger(Initialization.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    private boolean alreadyRun = Boolean.FALSE;
    private final Object sync = new Object();

    private final RoleRepository roleRepository;
    private final PlatformRepository platformRepository;
    private final GameRepository gameRepository;
    private final GameFileRepository gameFileRepository;
    private final GametypeRepository gametypeRepository;
    private final GameGametypeRepository gameGametypeRepository;
    private final GameMapRepository gameMapRepository;
    private final AppUserRepository appUserRepository;
    private final SettingRepository settingRepository;

    @Autowired
    public Initialization(ApplicationEventPublisher applicationEventPublisher,
                          RoleRepository roleRepository,
                          PlatformRepository platformRepository,
                          GameRepository gameRepository,
                          GameFileRepository gameFileRepository,
                          GametypeRepository gametypeRepository,
                          GameGametypeRepository gameGametypeRepository,
                          GameMapRepository gameMapRepository,
                          AppUserRepository appUserRepository,
                          SettingRepository settingRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.roleRepository = roleRepository;
        this.platformRepository = platformRepository;
        this.gameRepository = gameRepository;
        this.gameFileRepository = gameFileRepository;
        this.gametypeRepository = gametypeRepository;
        this.gameGametypeRepository = gameGametypeRepository;
        this.gameMapRepository = gameMapRepository;
        this.appUserRepository = appUserRepository;
        this.settingRepository = settingRepository;
    }

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        synchronized (sync) {
            if (alreadyRun) {
                return;
            }

            alreadyRun = true;
        }

        init();

        InitializedEvent initializedEvent = new InitializedEvent(this);
        applicationEventPublisher.publishEvent(initializedEvent);
    }

    private void init() {
        logger.info("Initializing database data");

        checkSettings();

        checkRoles();
        checkAppUsers();

        checkPlatforms();
        checkGames();
        checkGameFiles();
        checkGametypes();
        checkGameGametypes();
        checkGameMaps();

        logger.info("Database data initialization complete");
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

    private void checkGameFiles() {
        logger.info("Checking GameFiles");

        persistGameFileHash(GAME_COD, "./main/pak0.pk3", "8de550102fcf82c48b03401a9a8e71edcea9faae055313b925ab367537e4ad20");
        persistGameFileHash(GAME_COD, "./main/pak1.pk3", "9f38d57cf3aa8fbcfca7b1d1ceeb4dc607d69214eee04001f73685a5dcaf3ce7");
        persistGameFileHash(GAME_COD, "./main/pak2.pk3", "8caa5d3f01f0667e27b24f5a102b17f22dc0476f6b26226f95eb020a91c6a6ef");
        persistGameFileHash(GAME_COD, "./main/pak3.pk3", "4a94a890c84604099014bc313a9ef9c1a3aa863e068894625bbc0895e47074b7");
        persistGameFileHash(GAME_COD, "./main/pak4.pk3", "0c8b9cae5f76fe2fedb48f53f7b1b38b7af315d84bf2f092ab7883129c7ce73b");
        persistGameFileHash(GAME_COD, "./main/pak5.pk3", "3c9cdf736a82d7ed1e0307b90fb2cfb32688a80666c3e7856710973856b890b1");
        persistGameFileHash(GAME_COD, "./main/pak6.pk3", "062c7fbef95f32469d9083557724d7e06cd8a639f9a4d40635057b646dee5268");
        persistGameFileHash(GAME_COD, "./main/pak8.pk3", "4b81d18b3950d20176965d2cf7710f4ac2b7d4963b199c4a7dd6c943d4a4392c");
        persistGameFileHash(GAME_COD, "./main/pak9.pk3", "dd8d0258e208054983f89e77e0a2298944b04ad3e7240a51ab07869ac8986561");
        persistGameFileHash(GAME_COD, "./main/paka.pk3", "e6bf22889e51760b036ab404e2677da5d6acac4974406d47181c154a7f3f68f3");
        persistGameFileHash(GAME_COD, "./main/pakb.pk3", "5fc12f1cd13732ac70babba2be791ba261536a11f47354994780d733eee1bfb8");
        persistGameFileHash(GAME_COD, "./main/localized_chinese_pak3.pk3", "f85f3d93d1a698b887b84e0fa289508b41671ac73cdd544a4f7585f8024494bd");
        persistGameFileHash(GAME_COD, "./main/localized_chinese_pak5.pk3", "b3e18427c98696a7f6cd80caec54785dd236b4038061a80a35b89b3b99ea3d27");
        persistGameFileHash(GAME_COD, "./main/localized_english_pak2.pk3", "58247476bd0d53ba3653609136ed91e69be0f657dc27a253d80281ebcdd220da");
        persistGameFileHash(GAME_COD, "./main/localized_english_pak3.pk3", "4b388466e6aa6a6bd14e354b0a8195cda897914f3d67cda0bec4adc3bf40d9a0");
        persistGameFileHash(GAME_COD, "./main/localized_english_pak5.pk3", "d55073400da20f8a10cfdcc0e18997e56ce5a498f2e14d0756cf0bbd794d56c2");
        persistGameFileHash(GAME_COD, "./main/localized_french_pak2.pk3", "5560186a5e7f9847c1468b3c4f21c55df9089a09d220223657139cdb15e86964");
        persistGameFileHash(GAME_COD, "./main/localized_french_pak3.pk3", "8c02a3e9f336f983f4471cc7919a5f81c7819d853b52b485c78807f4030166e4");
        persistGameFileHash(GAME_COD, "./main/localized_french_pak4.pk3", "512168ea1bae91c77890c4d052b1ec2603b030d30c573c4771e022709b8110ac");
        persistGameFileHash(GAME_COD, "./main/localized_french_pak5.pk3", "21454f83dc0bfd0d8c4ec92cb151d6417b4d872744e55a10f136be63cb2bff57");
        persistGameFileHash(GAME_COD, "./main/localized_german_pak2.pk3", "470bdeabe4b58a94b188b6998ecbcaa72ea20b54855ad2478997b62e8bcf4d81");
        persistGameFileHash(GAME_COD, "./main/localized_german_pak3.pk3", "28c59b26a2b0d780e0239e3435ae486d8c9cc59e2131e23ae48b1a6b97f3c215");
        persistGameFileHash(GAME_COD, "./main/localized_german_pak4.pk3", "34dd885c9b9ed582100c357e4be401d3a6b268d4304811933a61ca85e9de955c");
        persistGameFileHash(GAME_COD, "./main/localized_german_pak5.pk3", "c93bc21aad00cff92c1ab1d0528783666e4b83ece3ac05b25193e2e0cb3dfe79");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak0.pk3", "f0ce02717130fba9f0bc740ae88eeee4fb9d6cefa7729671c001293a9446a1da");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak1.pk3", "18531524be7e62933278c2113fd5d1977a3ac8c1335c09a3363b3be532b268fe");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak2.pk3", "c24dea7508674d93edf8f3d7c3006d33a218a49760ee0b7c2ba726b766e7e82a");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak3.pk3", "74b7fa756d121154436d2e05e5d6739665e6c93530bb92d0901b3a0f6b083fbf");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak4.pk3", "a80cac70df5696127f79bdfa78ddf2596350d1b10cf6ee09d4e1a819790d2b21");
        persistGameFileHash(GAME_COD, "./main/localized_italian_pak5.pk3", "9270623bb2087a34bb25f70800694a3f253383eba32c45ccfe7755b12576611a");
        persistGameFileHash(GAME_COD, "./main/localized_japanese_pak2.pk3", "7b0d8f56a0427a132449a609b2fa65f1e2ae5b615b561687c098882fe4ebda9b");
        persistGameFileHash(GAME_COD, "./main/localized_japanese_pak3.pk3", "ed11d5635b0c5f5d5dede961d446b0031558c6a7b7304e404eef6b1b5f700f7b");
        persistGameFileHash(GAME_COD, "./main/localized_japanese_pak4.pk3", "0857d45dc8013abdfcf1faa82044781312d753a81d1aa3566fe8a54070d0ea5d");
        persistGameFileHash(GAME_COD, "./main/localized_japanese_pak5.pk3", "4c4722f5f340b147d79e06b64f1634bd911371b9e370599114e584445de3acc4");
        persistGameFileHash(GAME_COD, "./main/localized_korean_pak2.pk3", "1c6cda88c25a64c9590da0ad1f9c36eec78a5726050bf82164d7d83d1739576a");
        persistGameFileHash(GAME_COD, "./main/localized_korean_pak3.pk3", "04bef11bf2b5ada4706b760c15a1b20e7c332da9ced6d98c2c642712f785ea59");
        persistGameFileHash(GAME_COD, "./main/localized_korean_pak4.pk3", "af25f57263f2d6b5d7e0493aa37d12d4c20db29c75b63052e53580aeca6c40a8");
        persistGameFileHash(GAME_COD, "./main/localized_korean_pak5.pk3", "f7cc8ad44ad2ec36bb3b470ee503d670f843b216b8524f0e5711a2da80fc89d5");
        persistGameFileHash(GAME_COD, "./main/localized_polish_pa5k.pk3", "c936b15ff79db1e1026619eba24a09d5f1a9e135b182911b959fe3e800c4799c");
        persistGameFileHash(GAME_COD, "./main/localized_polish_pak2.pk3", "cf83a22136a6472519b5e86b93fdb6887554f2107b32f6f93dc12bea8ce1ed3a");
        persistGameFileHash(GAME_COD, "./main/localized_polish_pak3.pk3", "18f973c65fb762da24eb2c9e6bf9b34bf42c9d8bb674eb098d89013265c3b1f1");
        persistGameFileHash(GAME_COD, "./main/localized_russian_pak3.pk3", "10a94c3b405b20942ddd66f18f46307ffe0127a21eebd0f7aff52bf5a377cd3a");
        persistGameFileHash(GAME_COD, "./main/localized_russian_pak4.pk3", "254b29bde5506bfc2edbbaa64f0b874e103a90a3bae7271cada1c0c5b56f3e7f");
        persistGameFileHash(GAME_COD, "./main/localized_russian_pak5.pk3", "167571eaddd125206f657c1a9f050b67d091d0b125db53f4282b30961fc28a3f");
        persistGameFileHash(GAME_COD, "./main/localized_spanish_pak2.pk3", "f9ce47373622e3c78ddb6ed575eb072200715ceaf3e4c0a283cea40ea29be7bd");
        persistGameFileHash(GAME_COD, "./main/localized_spanish_pak3.pk3", "198ee138e746c11c503ada6491d0a8e5251cae52b758d793d0b7f867aa8de0e0");
        persistGameFileHash(GAME_COD, "./main/localized_spanish_pak4.pk3", "9a3a8a63902fb3b5246b6dfbb1505614e743323c35a5df23b619150908ff544a");
        persistGameFileHash(GAME_COD, "./main/localized_spanish_pak5.pk3", "55d04289912fbea915071729b2a136f05adb75b516dc0f7bbc0c1b7ebd189891");
        persistGameFileHash(GAME_COD, "./main/localized_taiwanese_pak2.pk3", "c8dc11fd609ef89b80f76f0e90480f42705805099b99a53428206408185ec342");
        persistGameFileHash(GAME_COD, "./main/localized_taiwanese_pak3.pk3", "8520016ee4d0894f6e2895459bb45f1814d0f6c9373fa1f44c0fa5f9f86531eb");
        persistGameFileHash(GAME_COD, "./main/localized_taiwanese_pak4.pk3", "a059d80f161e8d88a64f13690b75a1ca1711df5132c7dca243fc590bfe8cf750");
        persistGameFileHash(GAME_COD, "./main/localized_taiwanese_pak5.pk3", "f7dc9488d40d16b32bbbbe859dd86e25e7ee8d2a8b378221945ecaede30563fe");
        persistGameFileHash(GAME_COD, "./uo/pakuo00.pk3", "20bbcc0375492dad2f29543a1789116fa8db1b78c56e4a621922e6ef3ebb60f7");
        persistGameFileHash(GAME_COD, "./uo/pakuo01.pk3", "2f5d7688b9ea7f048ad8c629adc1587b003a6ecfd13fc7b583f3b64442796062");
        persistGameFileHash(GAME_COD, "./uo/pakuo02.pk3", "2d72e35df8f9f999dffe715a1a78b3345710bd82c4d2d903a1f7a3ff3a482b84");
        persistGameFileHash(GAME_COD, "./uo/pakuo03.pk3", "d16dc9a29ede6d6f887c0fcad3d0b7c5974ae2afc2de0cb87789e77e7f193104");
        persistGameFileHash(GAME_COD, "./uo/pakuo04.pk3", "f9e85eefc13c30e6edba9c63820dda013c212b7e83e9429237db02b529a4af06");
        persistGameFileHash(GAME_COD, "./uo/pakuo05.pk3", "0404829033a01aa9914b5b95a9029fa67e85e73c10422eb27ae37f753f2e4a9c");
        persistGameFileHash(GAME_COD, "./uo/pakuo06.pk3", "3d03816e18c460cfd4d4a9c70d04fa959f69953d4afd57317332e289255b71c2");
        persistGameFileHash(GAME_COD, "./uo/pakuo07.pk3", "61dcbfa7746dd1607f66bb7e329beb61fa78e9a849c196946187435cbbdcb60d");
        persistGameFileHash(GAME_COD, "./uo/localized_italian_pakuo00.pk3", "244364d5651bd31a575cdb05451ae712b0f0fe15964141e5c892f5b4fa17d63a");
        persistGameFileHash(GAME_COD, "./uo/localized_italian_pakuo01.pk3", "832581bdca844ae1b58df174410f756eff19e824963c55815f03d983ce747a5c");
        persistGameFileHash(GAME_COD, "./uo/localized_italian_pakuo02.pk3", "dc86f634c4123a98448c876331aff56ed5c9786760a743401f3bf543f2781ca5");


        persistGameFileHash(GAME_COD2, "./main/iw_00.iwd", "21824d61312e4d844db66ae518e050e5e72b2dd91c6335524bc62cd66cf3c86c");
        persistGameFileHash(GAME_COD2, "./main/iw_01.iwd", "32339f2be00a8e3e1d0352146e0da091a3287ac7edbebfc3cab3435288841f16");
        persistGameFileHash(GAME_COD2, "./main/iw_02.iwd", "b57765d51a7b35ee38cac4c16e10b7ad7cc920403199595a20e88f8c68a06a55");
        persistGameFileHash(GAME_COD2, "./main/iw_03.iwd", "b55b9daabd1d74237e855191bb7b3a8d0b466c4506e8e477be6d0e269c6596c4");
        persistGameFileHash(GAME_COD2, "./main/iw_04.iwd", "39b647800c49c05f7019d4cc43f851275217be5302d64377031e039047f7d704");
        persistGameFileHash(GAME_COD2, "./main/iw_05.iwd", "da8dd0d12c093b1e98b079d06f73ee8e064a7c5e7427da7842819be994fe282c");
        persistGameFileHash(GAME_COD2, "./main/iw_06.iwd", "c984d1e9f55feb6d58fe01d813e7e214ce59d5d36990f6335a5f1d0b3d71472b");
        persistGameFileHash(GAME_COD2, "./main/iw_07.iwd", "7e9627fe22370246320271c5947f638f6c80141b3f09bac0ab98feb06e993e79");
        persistGameFileHash(GAME_COD2, "./main/iw_08.iwd", "2d11dc6a6285f8d7822a65423f96df1292d8e729615b4677e8da4e829f72860c");
        persistGameFileHash(GAME_COD2, "./main/iw_09.iwd", "367ad45acd1b4519bc43a14c0d18f1a334f8ccd74d8e0e5bfa8d5a80d31cd46f");
        persistGameFileHash(GAME_COD2, "./main/iw_10.iwd", "c17022d6a4047356d84c631fe1cbb98ebe8de17033f555c667d7aae9856e1311");
        persistGameFileHash(GAME_COD2, "./main/iw_11.iwd", "75745855ae9810bbadee207a3e079535670a848a3459c5e6c132234c7b6b291e");
        persistGameFileHash(GAME_COD2, "./main/iw_12.iwd", "4862a976a1427f5ae9a2e3bb4368f847e091180f33e8ca09fdef5c81baa8bc63");
        persistGameFileHash(GAME_COD2, "./main/iw_13.iwd", "82c4f02f4d33408c0b6a87e6ce9fa27672b699ed25453b4a90564f945704757e");
        persistGameFileHash(GAME_COD2, "./main/iw_14.iwd", "50180fa4d0f78c0c41ee326e3603cbcf96d767f80dfdd11ef2edf9ad0b0e3565");
        persistGameFileHash(GAME_COD2, "./main/iw_15.iwd", "2c4b1797da9cc626b3dae99befa41dfbba4894dc14ce1199f5a459b88678f3ef");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw00.iwd", "938a92a9f9f01f5cc01dc2f506734e7f3906a8884e07e8bf3592042c9333a27a");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw01.iwd", "0280c00dc3d03aefe57c19d1f1a6e9dd800f05ac24dba7a22e9573947854c793");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw02.iwd", "31ef163463e3bd235f5ae8d1890bc8e0b62ab0c8db00ca96d77f1a28e3eb10d0");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw03.iwd", "7c56ea785409304877e6261f8d77f1edf346efeb93372abdb4c396a836d3667e");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw04.iwd", "8b0b7ae5f52679fae8d0dda0d6b6c33ab8bae1931f03e6ea72f377d4932a0166");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw05.iwd", "734dff9f5ea4bd693933dff6421d6618d95914de47a0da033dea928df394b2d7");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw06.iwd", "1beabd05f7af19e10a448351cf268ffc354a60baf1ff1573f0212fab340d7920");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw07.iwd", "25e250d6b860dd3438c5170917685df5055e3797a2dbdc752895f4be46b77e83");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw08.iwd", "b9aa3b2735cc48904cfdd42b36e089ffd5eff2f931124dfad110159ac8b030f9");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw09.iwd", "8d0029118b342ab70f49b3fad7d10aa5e98eee4e54909bc4456a21d760e3d5d8");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw10.iwd", "435701c7198455c008048f3598ffacba4b3ca060ce4c6d3362aca053621dea42");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw11.iwd", "8bc30d0e4bc1cfa54daa742d6520d7eb6a7da5b97bc3a73370dfd17dd8c0d127");
        persistGameFileHash(GAME_COD2, "./main/localized_italian_iw12.iwd", "afc5a68ca2ca18e8aaae9da5536ae1ed0e19c70bba09217b43b057873ac685aa");

        persistGameFileHash(GAME_COD4, "./main/iw_00.iwd", "95d8497f5c7ab62c8e715810c58675e3b4af51ef4c499882e720430ae07dd8f4");
        persistGameFileHash(GAME_COD4, "./main/iw_01.iwd", "984c0573dfeb5a5531b0e75d4305379f2bacd3ab68690ccf904f72a26ff8b369");
        persistGameFileHash(GAME_COD4, "./main/iw_02.iwd", "f3e347403a31ffeaa87caedd019955f87924c2a3b7b94999f2d5f41452acc698");
        persistGameFileHash(GAME_COD4, "./main/iw_03.iwd", "eab1c2165731a06fec80c1875a8c0879f1712e3154f434e0c600de50edf5c26e");
        persistGameFileHash(GAME_COD4, "./main/iw_04.iwd", "85114736cde49486c3f9be62eb8bc0764ea966f9f2a16f2686f7b4e850bb6177");
        persistGameFileHash(GAME_COD4, "./main/iw_05.iwd", "b15db07a430b90c6fe60306bbd70b62eb8ca00abd63fda81c71b3b7be1d1aecb");
        persistGameFileHash(GAME_COD4, "./main/iw_06.iwd", "074ae94032b228ddcd43df4bfb33f98516f12faaa678502d36ea4f83614ac32e");
        persistGameFileHash(GAME_COD4, "./main/iw_07.iwd", "76cba12ffb861291619c0d4381813cee330ad04d76569549dc0695fb99d860b7");
        persistGameFileHash(GAME_COD4, "./main/iw_08.iwd", "1588068e0545680205a398223d3d80abaabd1b76c690aa61267b233ba4de821b");
        persistGameFileHash(GAME_COD4, "./main/iw_09.iwd", "b600a86ca1cd2b8070852d4178609243b21c15b5cf370f4c0f96050999e97d42");
        persistGameFileHash(GAME_COD4, "./main/iw_10.iwd", "3dd3529f65d20b28f827f555f5e74d48bbf5abccd1ebf51b309b9c6fc3a7458c");
        persistGameFileHash(GAME_COD4, "./main/iw_11.iwd", "cc1d4781f8f99a0de91322b9b5f7864042b42d22f76fa90f16dd4e80cc9774d2");
        persistGameFileHash(GAME_COD4, "./main/iw_12.iwd", "a63bcfc3f4248acc63220743bced2dd2fb9384c782521b75a4dbe21f0ee2d4b5");
        persistGameFileHash(GAME_COD4, "./main/iw_13.iwd", "42a4de64e9b2504d2e7af9ced686a396bdb9c2eec7213031f836f3dc529a1166");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw00.iwd", "fa256a3f6b2dd8829c5830768ca303a8ab49414458ec5a7699c2c3572d7935c6");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw01.iwd", "aa0c7248576d3e51acc9912c5b467f043a2c78f74f5b50ab1367ba90f06728ca");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw02.iwd", "978d68d0cc776e184d8797a55b33003cc6f1182f2271ea6869deff553dac409e");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw03.iwd", "b910f86492a1908b6dbe7be414479cb03e156057a8015e54b1c97d981c806a0d");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw04.iwd", "a480d423b8b218f722bfa6fb357119cc7834e09bf291c7b2642d92bbc75b44bc");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw05.iwd", "77a1c977b3c806efe2e12700de454b9e6544257ef46434b16c53461186106fc5");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw06.iwd", "e436509aeefcc60dbd68fbc8ee66f825d53a81a42fc768134999f46e3af9d36d");
        persistGameFileHash(GAME_COD4, "./main/localized_italian_iw07.iwd", "498a1bb7951467e0b2de8687f7294bf2dd927862880eb73bbe84930c631e8e6f");
        persistGameFileHash(GAME_COD4, "./zone/english/code_post_gfx_mp.ff", "5a95e2d10a51c9dd872396c56e1e319a4e68ac9018ec36651d5862f953af290c");
        persistGameFileHash(GAME_COD4, "./zone/english/localized_code_post_gfx_mp.ff", "116c1ef51a5a325be75c43e50e440a74f25ebc366fd307a99f184f4e2a480209");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_broadcast.ff", "4253d87c2b389547ff7f7e3978bd3859ac30941c38d2c6d989eb3d1561f20f95");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_broadcast_load.ff", "a7e0e7b216f947d4d42b5677eb9b8d3b0a8c699937c4e9e151a544f422ef6081");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_carentan.ff", "ec1d008655a06b233ea58c59b9072032c2f9fb6f28b0b4a9615735ae746fc26a");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_carentan_load.ff", "29f2db48ebf10e6d32adc1fa1867133054dff6c5556d811f5116e50ffd2d1b87");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_crash_snow.ff", "9234aa05ad978b7a83ebc8ecfd3f208e7a793f975c9c3d3992620e1b6d50e61d");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_crash_snow_load.ff", "7c0bf0213aef1cf92ea2e74f484571ab75a63df1f5a86a8bfd274e30b15db3f4");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_creek.ff", "a79b51301f3ee922628d860cf191fa208483fac445b1d5891605d40ccd410a57");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_creek_load.ff", "c88be6d613075155eeb8f888fbe24c73ea123793d3a79f7dbf4d4457a9a13894");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_killhouse.ff", "4e9a7ce3090650e344fcba0c9c15b5fc8b3cfbe0013ecb0cf78819044284dffc");
        persistGameFileHash(GAME_COD4, "./zone/english/mp_killhouse_load.ff", "f4b8296348f8c09c44ebecb2099613411b3f94f2cfe7dd81b881e6db18f0b568");
        persistGameFileHash(GAME_COD4, "./zone/english/ui_mp.ff", "5ab5c7d1593f8c50d34db9b379ba186561b8359550719641ec5066e67d52c60e");
        persistGameFileHash(GAME_COD4, "./zone/italian/ac130.ff", "d75524608980f1e08d0088c62675a03ed42b139ecbf3fdc6230bb0cf513a1d40");
        persistGameFileHash(GAME_COD4, "./zone/italian/aftermath.ff", "474b150de7f9ef686e6d7f8d8ef7308c11b37c273cb4ea5c6ac0143cfa75dbf6");
        persistGameFileHash(GAME_COD4, "./zone/italian/airlift.ff", "f10c067a5bc550c2de6e6699c109accd542113f4b20c6005bedc98f43af73e2c");
        persistGameFileHash(GAME_COD4, "./zone/italian/airplane.ff", "d80fbffdf4ff7eec108b63b8880f6cf54ff17e859d62e54d9f739871fc27eab5");
        persistGameFileHash(GAME_COD4, "./zone/italian/ambush.ff", "7e9204636ffafcfd28023399c224e1f91755daffe09702c6eae45f1f0bcdd299");
        persistGameFileHash(GAME_COD4, "./zone/italian/armada.ff", "432fd0499bdec2b54ced03c21ba395c335f26ce0aa001d0b90435938c54a4ae7");
        persistGameFileHash(GAME_COD4, "./zone/italian/blackout.ff", "d0f311d6d64cbccb0c8334a30690b8a45767b798f88a81e7314730b1faf6c5e7");
        persistGameFileHash(GAME_COD4, "./zone/italian/bog_a.ff", "523d3643532d882fda5f58fc8b6c33ea877ceef33e0fb00130c89d5bbf1a9058");
        persistGameFileHash(GAME_COD4, "./zone/italian/bog_b.ff", "6eb0fd703e1e5b78e80682b96fb318d7dde49b112aca5644f93f30d977fc04c0");
        persistGameFileHash(GAME_COD4, "./zone/italian/cargoship.ff", "4c8bed1596269b9aa12f5d8c5e3db6281d168be8cb0cba0aa731453d31bb7e01");
        persistGameFileHash(GAME_COD4, "./zone/italian/code_post_gfx.ff", "74b2c27e5ffa5b73bdf0c9c83545a58782e887db5a9b9b4a0ff3d791a9838159");
        persistGameFileHash(GAME_COD4, "./zone/italian/code_post_gfx_mp.ff", "5a95e2d10a51c9dd872396c56e1e319a4e68ac9018ec36651d5862f953af290c");
        persistGameFileHash(GAME_COD4, "./zone/italian/common.ff", "a3be61940ef14f7bbdf37f47c383c8ed7abeed91b3cc38ab727a478a28a5cf71");
        persistGameFileHash(GAME_COD4, "./zone/italian/common_mp.ff", "602c068b34f6a32147bfc7727b4f18b6a37fe47b4e99c68f5e25c6507c637320");
        persistGameFileHash(GAME_COD4, "./zone/italian/coup.ff", "c4760fe294ad9f0e4cd1c4696dbc8bf581b5d23f98e84c89f32ac4ad13db03ce");
        persistGameFileHash(GAME_COD4, "./zone/italian/hunted.ff", "7a300efc07f3dd4a22c953c2d830154aa834f9d94d27ba7b796af1b4afbb7d2e");
        persistGameFileHash(GAME_COD4, "./zone/italian/icbm.ff", "a5aee98b118168789badd60d08386b5b6deaa8def30896e8b862c759a033b8fa");
        persistGameFileHash(GAME_COD4, "./zone/italian/jeepride.ff", "c425ec58d12717a36026f662fd3aa56b5b55f738620470fd69caccc470cebdaa");
        persistGameFileHash(GAME_COD4, "./zone/italian/killhouse.ff", "1c1d61a780709b1284d8bf3320dc9bf7f36ce8e71572c877c4913067bcbf027a");
        persistGameFileHash(GAME_COD4, "./zone/italian/launchfacility_a.ff", "ad7b1da63e3649815f54e09c6ada2bd0444671f9552aaf0aa5aebfc93a348812");
        persistGameFileHash(GAME_COD4, "./zone/italian/launchfacility_b.ff", "9c164f83527753de6a5b799730b67ac5d15a8685d1110541d3b1f15ee1ff8246");
        persistGameFileHash(GAME_COD4, "./zone/italian/localized_code_post_gfx_mp.ff", "233fcdbeb07e1bd7035088d361513e31dec3fd89f892b71ae6e0628f07ca8187");
        persistGameFileHash(GAME_COD4, "./zone/italian/localized_common_mp.ff", "e94346bcbb670bea8fef4852959d522ffe14c96eb322c431428daa180f5cbed8");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_backlot.ff", "cde1d125196c060f22450d44259e2372c20784fc4519087644866df1caebfa52");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_backlot_load.ff", "0ea6721cc6ee0631dbb394f9ba853718d7450657bfacc9702f66224738e4d767");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_bloc.ff", "a67c5183bed3490b8c52e4730e64c7d2ce644d54a8058945cf9c87842c43f03a");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_bloc_load.ff", "27c1bf437569f09b7a99714a7864fda55240a22b705f85dcdd90904140a445c6");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_bog.ff", "81818c373f6cd7c967d6990ddaf9bb6ac253febda6ae92278d6036acf63bb3d7");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_bog_load.ff", "747f369406cc6b1d47b1c3c5564027abfdc278d6a57efd9d803297c19202428c");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_broadcast.ff", "4253d87c2b389547ff7f7e3978bd3859ac30941c38d2c6d989eb3d1561f20f95");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_broadcast_load.ff", "a7e0e7b216f947d4d42b5677eb9b8d3b0a8c699937c4e9e151a544f422ef6081");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_carentan.ff", "ec1d008655a06b233ea58c59b9072032c2f9fb6f28b0b4a9615735ae746fc26a");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_carentan_load.ff", "29f2db48ebf10e6d32adc1fa1867133054dff6c5556d811f5116e50ffd2d1b87");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_cargoship.ff", "f3fbb616100b674116fc70bf70371399336172e5aaa872704565e1dd8a71ecdb");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_cargoship_load.ff", "f1cf274a54042c84f6557e451e0dfbc9ffbcbad5f39f5d5a01a1da4a31a86724");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_citystreets.ff", "afcf1d4b2313cb2307bf185bd411cc52b78bfd488b11b895d38c99b0ab3d1d3e");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_citystreets_load.ff", "9e2e4452f3f94216f7bee11c12a9955ed297952478d018a646c49ea5976614af");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_convoy.ff", "d0ba052ebcd5412dd14efec2f3c6f3cd1a6cb7ea837cb435dab54a3874a1bcbc");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_convoy_load.ff", "40ac610bc72a7b265f2712fd0dfe03ef86e78ff31d99423c3685b8c26b8cbc51");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_countdown.ff", "80829ff718a0c1340ff7453aa21e74d5d62f70cbe93ed382da98c448e80a1365");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_countdown_load.ff", "145f74b7f23d61b65e3e1cb76bf9dbb65a7114e98585293e7a878e762d422691");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crash.ff", "99a91df69078bc11d31de3afb5c3ba56bbefb79854a4d1417d5fecab0dfecb68");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crash_load.ff", "00df8e88d33df56486d0c5a94c0ca528ec4eb5469720a08465df9406692b883e");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crash_snow.ff", "9234aa05ad978b7a83ebc8ecfd3f208e7a793f975c9c3d3992620e1b6d50e61d");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crash_snow_load.ff", "7c0bf0213aef1cf92ea2e74f484571ab75a63df1f5a86a8bfd274e30b15db3f4");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_creek.ff", "a79b51301f3ee922628d860cf191fa208483fac445b1d5891605d40ccd410a57");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_creek_load.ff", "c88be6d613075155eeb8f888fbe24c73ea123793d3a79f7dbf4d4457a9a13894");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crossfire.ff", "348fd99400d97ac47ed39c580b8ed466fb9ec1994f1e3399df27824e1e1c08fa");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_crossfire_load.ff", "f50d0fbefcaff989ceee91d9962dab7636c060c5fe3a254fce0d8bb14139947e");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_farm.ff", "ff1b5b1cbfd4a8217fe8c940ab53894e8759c59efa642025bb7d0f1477bdefda");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_farm_load.ff", "1f75d8d4ae1c03220ad12799abf2557fd59c1030f16fc059850839c53912a8f2");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_killhouse.ff", "4e9a7ce3090650e344fcba0c9c15b5fc8b3cfbe0013ecb0cf78819044284dffc");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_killhouse_load.ff", "f4b8296348f8c09c44ebecb2099613411b3f94f2cfe7dd81b881e6db18f0b568");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_overgrown.ff", "710bc959f9ccb22eec30fc486c19b0f4afaae171250ff26eda46d63abef6ef29");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_overgrown_load.ff", "8e82e22373946bbb6a366e514b538b0a53104197212f2b2bee0b43da53daac75");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_pipeline.ff", "cecd0b7cd321a2cce4d33d2d4b73b3061e8557dd5181e344cc5028120d61336a");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_pipeline_load.ff", "340cbd473bdc89614a265e0a1ec45bf1d51f81dd3f635436a7b1d3f1643054ff");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_shipment.ff", "ffd4bd0c6327b989bf86adc9a000b66575bd3208514ff96109bfd8b3c8b671ed");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_shipment_load.ff", "94fc8fc150c9547139adb3463e8c9caed848fbec55b5bff768e7db927e760853");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_showdown.ff", "5fc28be6c6c3aa8c1c524df1331dd894af9dc9c4650962c3ff25dbf565eb11a8");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_showdown_load.ff", "c54cf42d6306d0e79df7dfd42cd4420c356f1df2d20b1398d77417fe0db2efa9");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_strike.ff", "630d44817e1c06b4fc644a418bc20919a346c3487fccd6919f9a7f5fd9e8f2dd");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_strike_load.ff", "4e438beeee9d4afce093941573896ac0da2a0e44e59d99c36493eb0c208f64c7");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_vacant.ff", "1b5d8d13d250ab15607099de6fc7d96a9c92de9e826d45df34be34dcc92a6b8c");
        persistGameFileHash(GAME_COD4, "./zone/italian/mp_vacant_load.ff", "dc8e37fbe699cf6e51323c795db80b8720b93ff8630e0a2715b23f27816cd8a6");
        persistGameFileHash(GAME_COD4, "./zone/italian/scoutsniper.ff", "627d8a09daa5ed617f4196f65614bdff148c068a27f93d725dc6875b3d3dd4b5");
        persistGameFileHash(GAME_COD4, "./zone/italian/simplecredits.ff", "5c558af370037a3cd6340e06b559e0a85401ea1f4c5d8d75211867f14d7743a2");
        persistGameFileHash(GAME_COD4, "./zone/italian/sniperescape.ff", "d594e4b118b765f4e95de71c96c4b33254faedc5366836a5a037a361f64cc548");
        persistGameFileHash(GAME_COD4, "./zone/italian/ui.ff", "f961af1e57edf733a513a946648f3a9ad14e991950392bff6f08823020dc54e4");
        persistGameFileHash(GAME_COD4, "./zone/italian/ui_mp.ff", "5ab5c7d1593f8c50d34db9b379ba186561b8359550719641ec5066e67d52c60e");
        persistGameFileHash(GAME_COD4, "./zone/italian/village_assault.ff", "e8fc1e7ccf98e9ba9add8b918e10f2ea3a004cad3a9bd4c70667f019819478d5");
        persistGameFileHash(GAME_COD4, "./zone/italian/village_defend.ff", "2f07e4df4cc7f239be57ef9ef37cbe28dc0ca813941829ac7f4b76eeecf4d7ed");

        persistGameFileHash(GAME_CODWAW, "./main/iw_00.iwd", "b576780026469bd71aefa7c1f2e10244bdaf530912ffe08057e3cf0f5c2d39ed");
        persistGameFileHash(GAME_CODWAW, "./main/iw_01.iwd", "e9e2d0a719b9eea53b889ac61ce87a4d73c5b52312d95d2dcf40b1ebd25238b0");
        persistGameFileHash(GAME_CODWAW, "./main/iw_02.iwd", "30d149c2f3bc829a6b8e2ed88e39078fdebaf3a42503dbb48d5733afea5b7b16");
        persistGameFileHash(GAME_CODWAW, "./main/iw_03.iwd", "e538cf8adbcb08f2d66096f78ca50e109b721179a0f20e3a506907c575a5dc51");
        persistGameFileHash(GAME_CODWAW, "./main/iw_04.iwd", "b5ce6ee46fe1484b1da15f17a4a4df4da84b128b3846500759a0e7e5e8d22740");
        persistGameFileHash(GAME_CODWAW, "./main/iw_05.iwd", "bfd49dffadeb1c7acc9eead11288a12fb5229d782c9ad8e8ee5778cedd976649");
        persistGameFileHash(GAME_CODWAW, "./main/iw_06.iwd", "4acb4d45abcf251bb40dbb27ea20b93a85b2648815aaa890732ef2b781bff4f8");
        persistGameFileHash(GAME_CODWAW, "./main/iw_07.iwd", "ae8295133e75cd11af96b8a2008778b307d99f2f1f9b450ea0f1110a309b77a5");
        persistGameFileHash(GAME_CODWAW, "./main/iw_08.iwd", "1d5382dc1c8ff5589df3c57a945a594c6d4646cf877364be2d642c134501f98a");
        persistGameFileHash(GAME_CODWAW, "./main/iw_09.iwd", "f7df6c3066a2617aa9bcadc821f496105c19a2ef97529febd2f6bde46dde98e3");
        persistGameFileHash(GAME_CODWAW, "./main/iw_10.iwd", "01a13b0c4a393eb0bf34fd76f9aa31db820e0f9c3b1c8a1a097226b51f441afc");
        persistGameFileHash(GAME_CODWAW, "./main/iw_11.iwd", "320f459cb0891e514e810a1e58e22a6cce06dc250ee6bb3a443be8a43cdf2630");
        persistGameFileHash(GAME_CODWAW, "./main/iw_12.iwd", "8a94165747e9c5592b552a92ae1c1bcd79e79821dee3bfeb7bc1289d893b1076");
        persistGameFileHash(GAME_CODWAW, "./main/iw_13.iwd", "683a727949623cd69d8fda948ae5bcf97e76fb41c7aa386a558c3f460a36c948");
        persistGameFileHash(GAME_CODWAW, "./main/iw_14.iwd", "e177a9968a5f9def69bd0eddd285ac411262801f1fc9be2e4c916dc45bb80c1b");
        persistGameFileHash(GAME_CODWAW, "./main/iw_15.iwd", "1a895cbfff63a3622d27f780ee62519797e1ea7c23ffd35259122a33dcd9fc1d");
        persistGameFileHash(GAME_CODWAW, "./main/iw_16.iwd", "a73f022a6ab079292b95d92e626fe521a612fea820f2606a1d0c70f29025c57d");
        persistGameFileHash(GAME_CODWAW, "./main/iw_17.iwd", "2d8cdf567497f4c00f91c38b37089116b98feabe3a9d4dbe45b1cbdec74d355b");
        persistGameFileHash(GAME_CODWAW, "./main/iw_18.iwd", "8beb6a49526f23613c1436ae196d49dd2d760cd28e60e6a7545f8313ee4fac29");
        persistGameFileHash(GAME_CODWAW, "./main/iw_19.iwd", "f23bdb3c4e919c3653e4c60ddb79e77f2a431b8c199c6b6715ac15794c63faea");
        persistGameFileHash(GAME_CODWAW, "./main/iw_20.iwd", "f07be2295b8d479c2066ae012f6aa3029430ddf329ceb812e7cfd7ba18f11ed9");
        persistGameFileHash(GAME_CODWAW, "./main/iw_21.iwd", "08f3de3ae473e29f71f01d04b5df220fdba0322719437947c1a928c2c30e4e42");
        persistGameFileHash(GAME_CODWAW, "./main/iw_22.iwd", "d86d6eef271051cab33cdb25c9508ef1bc807cd2ce4f658440d0079c254866e7");
        persistGameFileHash(GAME_CODWAW, "./main/iw_23.iwd", "64488eb380767e6ad367aa3ebd2bd06aa16c864a9cd0d64c2b77b14c0ef4b9ed");
        persistGameFileHash(GAME_CODWAW, "./main/iw_24.iwd", "231c900401d640c1f305f24bef9bd1b10b39cb0b2ff305adc62b7fc52394f243");
        persistGameFileHash(GAME_CODWAW, "./main/iw_25.iwd", "717641afa67b8e682b1a8d4a54268a215b3461e7bf5c5ff4f39d5af437475c23");
        persistGameFileHash(GAME_CODWAW, "./main/iw_26.iwd", "512b62c11fe051d674d77b066c5db0decbaadd1d1b4ec12e24867096c5ba4037");
        persistGameFileHash(GAME_CODWAW, "./main/iw_27.iwd", "1729bfb117b7664359011be060fb9aada490e7d9602f755751d4a8c469eb41f5");
        persistGameFileHash(GAME_CODWAW, "./main/localized_english_iw04.iwd", "a067cde63b7c1828a8cb638c5ccad984b75ca27c0231099eb4863c66ef0b5f09");
        persistGameFileHash(GAME_CODWAW, "./main/localized_english_iw05.iwd", "a94ddc5e5b81aefc11f9c860724abd8dac8ef0f0e910bfa60193e28dda68f953");
        persistGameFileHash(GAME_CODWAW, "./main/localized_english_iw06.iwd", "f956540850fb082b5a8ee53db4d6c3d20f65fa4c9885de8c04beb4a26333760d");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw00.iwd", "f0226bfa4acac27e11d47e1f38c3eb31c6f5b76d5a89193a84f3536dd08a3133");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw01.iwd", "f5e9f030d3e09108d06c755f6c885efe8d4575214f5c947fd6f2ef4f544e3892");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw02.iwd", "930a0b0669468a75c021e8df6683ed9814587f81838f5480a397241b7b57b8d0");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw03.iwd", "f166e71155808c3d595e068c285748fce55e57f4340af087baef2770ea3f57d5");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw04.iwd", "850af9c28923883577481a2f56f7e45f51774947c1793317c4ebf82386be6d49");
        persistGameFileHash(GAME_CODWAW, "./main/localized_italian_iw05.iwd", "5f100d8157f9ac4251ad4c0f89cdf23f265bc51bdd55bc561700e6f056d6e118");
        persistGameFileHash(GAME_CODWAW, "./zone/english/code_post_gfx_mp.ff", "d5cb1a61466763181879c96e4c5028a333af3cdb06813ffb97c4382672555186");
        persistGameFileHash(GAME_CODWAW, "./zone/english/common_mp.ff", "165609e74216f3a1656b97f49a80b8d9148e518e63a721c1351efaafc0b1940a");
        persistGameFileHash(GAME_CODWAW, "./zone/english/default.ff", "6455556d458ff538a946c54ccaa0caa969741f469fb8d05ec7c6fbce093b07a8");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_code_post_gfx_mp.ff", "997a9491439b1e6e70252ae9b664f264d046ab16b3e9fd3b89926a1775ebbbe8");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_common_mp.ff", "26fd3f85e954263831525dde0946a36dfd2ed998dc71f3ffa743884b08155f5f");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_bgate.ff", "fb3343a5f0d633bc24a3b802218e25974e925a05d8d16b2b940bd10d61ea4f32");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_docks.ff", "b4d510693ca86dadf0c54998d154e18b5285e81bd6fa80536f5adbf1c40227bf");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_drum.ff", "28ea08e61a3188b1691867130f9f2be9cb34b894d7ee9ff03db1168bf6a321cc");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_kneedeep.ff", "d6bc1ca95b84dc76c486ba6753fd5ec8c92978a5a32b3e9bd76750ee1b06457e");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_kwai.ff", "2db4fb2f2406ff180cf79c78ce4ceb2474853b6bad8e85f7a6519034b6bcccba");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_makin_day.ff", "5efd66a19e18f9e7aacff75c19d4bbf44927d91fc24e1ef3116c84b731d9dbbf");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_nachtfeuer.ff", "2b88b10ced49996514f30f6b112e49696ca183b833c5f1bd9307f0e5116608bc");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_stalingrad.ff", "4c788481b5f63951f701898c52e00bbf3bdbd27b935675fb02f9b713c941a260");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_subway.ff", "4f8c693d4c7833d7383a1c86ec924dc7e789bef232292dd38cd8ce43a6baafb0");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_mp_vodka.ff", "b3e8dc54b1a7e932570b6110e745e63b01a5c0eac53fd6e736d1f8fb5b06ee32");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_nazi_zombie_asylum.ff", "d0ee8e0764490135f6f7e47ffb52f6f81715c47293a9db797d3f81feeea356bb");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_nazi_zombie_factory.ff", "7c5b6517785589e0c66a0535cddac5afe4d3093910b23cd0a3a0257f75f37034");
        persistGameFileHash(GAME_CODWAW, "./zone/english/localized_nazi_zombie_sumpf.ff", "3af29139ef26dedcfcda496a0aef84507a84161c90abc37ffd8200a005794c22");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_airfield.ff", "f535e4819d431d9bae9a47be26ac2fbfd51c0db81282d0c0e84edfb9cf8eeafb");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_asylum.ff", "7b338947c088cd5ac6f93b4ceeb1ae27e2d98b20e6c4806c150818317caf7664");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_bgate.ff", "ca9fda6ef8ca14eca4baacb852491eb78bf1e68c6725cf588e93c936c7de6baa");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_bgate_load.ff", "dddc9d91a65cacbe63747cb55651062c7e0e68c12323d326e4aa3c865013e89b");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_castle.ff", "ca45b22a5e1a555b9217fbe27228608e7495966b4c45592ddae151bccd63bf43");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_courtyard.ff", "725c36778fe1f9e65ea10a2377862de4fd1e38a2c9fa887fe62dcf705b6d4143");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_docks.ff", "f47b900fac1e6c1bab227feea5d81a3403a28f016265c7266e83df74cb6fa062");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_docks_load.ff", "1c90f6112b6d9621d871e4c411fcf9d0ba206d6667a9b40cc7e9b37ed26d38c4");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_downfall.ff", "8f678af09c1ffcd4ebab92ee895529b47669b78f97dbe7dfdb1da7aa4baffb19");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_drum.ff", "462738ebbc99ffbc5dc39f1dd02b3dc1b82a12d1736a8121ef9e34d1b92493a1");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_drum_load.ff", "2adb5bbfc7c9d473f6d23690b57ccdbda30f758bdb15081dd6b5e74453b73e0a");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_hangar.ff", "01fb5fce6b52497d03de0e57643d9005c245295644c011f8b02f1b0cd0aafff2");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_kneedeep.ff", "14d845cebd9a93467bbdbeeb8662a92755ad09d0e75121d358e2b5a88151fe22");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_kneedeep_load.ff", "cffafd2b42484d2c5ea7189da286f978e6ac2eb24d4b8aa7fe530ac776d72222");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_kwai.ff", "a967f24855c17011207f09dfc3aa4698cd425a49573352d653eef9d3b6860e83");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_kwai_load.ff", "a49f172f9b3b65b117dbda6ba0679d69e00e70b5b09f9498724b861b34ae600c");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_makin_day.ff", "5e2ba1f4084d2459534cd3afe9207b7b59f580379b8093f217f9116978efa41b");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_makin_day_load.ff", "0ef75dde847cef1991021bdf99c1f2e93b2d85185a468cb19cd1e757ddc68199");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_makin.ff", "e512915eb3d37bed5f502c4f9999cb3e08eb1ba3785c77e61f155af4d0f69932");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_nachtfeuer.ff", "fd5ad11c9aba6ba74ca4efd02391170798f4a054bb9805bdb656747a70049c9b");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_nachtfeuer_load.ff", "8d062cdfbe57092bbbd34df5e826371d8a275888b6d76e9eec66a081719ff36c");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_outskirts.ff", "3292c85b2521a85655d68b0573b276146a4492ffd1056c1c392ecd2e5a1ce3b9");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_roundhouse.ff", "b7114f427657145e80316750e4527392bf7bb5e18063241d51c7972812a4c40b");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_seelow.ff", "12ae3a86b5939ed0957b6da214c6ed1dd9de9c800920df119c50422e6a9231d5");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_shrine.ff", "d75534b6c22334636c5b525e2571c9bd5398a78346244c80a54d6533e76410f4");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_stalingrad.ff", "2d88faec464a49f533b720f8ed7a6ed4d4abc0649b9ebf2fb1ce12dc758617f9");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_stalingrad_load.ff", "1ea51e89954696f38df0c981a20b51e5fbcb0ac32056e86ca35bc89c2c2a0f04");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_suburban.ff", "02daa316a24bcbc08da0fec076427b4219bd99b8a6ace2c480618e79a18be8b5");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_subway.ff", "3d533dc138d54a218ddded9900352ca1f090e70081a6b5a85ee0a30c1c6caf31");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_subway_load.ff", "ff2ebb60d2dededa267eea4ece871edc93517daf71556f0124aa14abb8b2760f");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_vodka.ff", "5363259786823d3e8e5ea1a781ebeee839451b5fe570142d5a37f51f3f6625bd");
        persistGameFileHash(GAME_CODWAW, "./zone/english/mp_vodka_load.ff", "551fc00b2fc3fdf40eec8c1f4401ea42ae92d105dfc2e66fc8677bed8378a97e");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_asylum.ff", "62707f92222217f3998c98a0a1681323260a2b0be50697c275669200a9b4ad65");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_asylum_load.ff", "98e0822b8a6f4dd2b7da8a11c537b807e37fb929edaadd149758cfbe90115d02");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_asylum_patch.ff", "c4c1df754bb2625a69e866c1d6ccc90cc903bbde2993b02c52e67f817e738f87");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_factory.ff", "7c6286743adbf7431a7945798559be3622adb03c99e36f64fe557f6163afe902");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_factory_load.ff", "8c1c2ca633dd72ed3a139605375b646ba50d9a94811c408061ba6ce431e62559");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_factory_patch.ff", "a97ce02875eb97ed04f1a3df1b40c59065ddd2a567f1cf01c8b4413c6cd5e1f7");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_sumpf.ff", "062e5fefd0f86f96e64f0abfc9a4507c4844043f08ac21eced85c2fb20fce0db");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_sumpf_load.ff", "28e4fd594caaac30b3bb37c1b9ba047aa97ee65b7bf2899de3a4f06ab361afbf");
        persistGameFileHash(GAME_CODWAW, "./zone/english/nazi_zombie_sumpf_patch.ff", "6cc3c4454698c426d9bb70f14ae1ff5f7ecc5dcf171d87a712769ad4efe304e5");
        persistGameFileHash(GAME_CODWAW, "./zone/english/patch.ff", "4458199ac93bad0737c6b5d54b79b45484bb7d0655bdaa55f4e30d66ad579135");
        persistGameFileHash(GAME_CODWAW, "./zone/english/patch_mp.ff", "c282efa039044774567da841eae89142c33e9189ba840639c22077b5b55e0870");
        persistGameFileHash(GAME_CODWAW, "./zone/english/ui_mp.ff", "6039baa4b631aff24d11f224f95f702e182a98334b8e4c618f452146d95a3642");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber1.ff", "6f46a7f4791d721b635b53be35eff2592ffdf498c4caf5bfbbd8efc23ae1db4f");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber1_load.ff", "77502c463d468f6bf9a9cff23f0f4410f5ed243bb720e65474040bdedc7a5bab");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber2.ff", "ed6f2c0248eeef59b55ccd409d0a23842b171871fce875bf9a41a735341d818a");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber2_load.ff", "105be4fb3827719a755ab465099e43fcf0fad656eefef2fd9ce337907c6f1139");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber3b.ff", "4cbe5347f1d486926e13793a95258c1aaafd1bc2f5dce2d420190a69ff76b04e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber3b_load.ff", "cef831538ad5321034452462ad49f8024b5333059597287ed9eb5fb01b5da9aa");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber3.ff", "5e4aefa25865099132355f256218c59e785942b4ca08db7cf5ae0d94d9a235e7");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ber3_load.ff", "71492ea9834d7f5b2473243c4b3c8dc3fa7a1b4eb3f4b17bf52671a9874886e7");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/code_post_gfx.ff", "25adf66f2a7b1324c895bf372d1d48157a24da12619427a2bfa0e2cbfe94e1f5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/code_post_gfx_mp.ff", "d5cb1a61466763181879c96e4c5028a333af3cdb06813ffb97c4382672555186");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/common.ff", "bc1522e4c80e5b33afe1850886817cc02ec9e8dc675c3a6467c4442d6ce82dca");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/common_ignore.ff", "639d6d307e467da45cac50715bc37d4ee36f4055c955fa6916de48a0c3b9d743");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/common_mp.ff", "165609e74216f3a1656b97f49a80b8d9148e518e63a721c1351efaafc0b1940a");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/credits.ff", "1301aa6271eed704c77c7c83a6c6fce419932565d6681871c936a4e0eeb2706f");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/default.ff", "6455556d458ff538a946c54ccaa0caa969741f469fb8d05ec7c6fbce093b07a8");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/intro_pac.ff", "15127c449b68a8ecca7995ca1b67439fd6ff9d12d13f09ecbb45c4755727cb5e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mak.ff", "0ec123a7b26b74852912266775d6d7eaf090c592f4e9266c46c5fc4f3d0b92bd");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mak_load.ff", "1ae3129ab8373f28b3183155752be8d0d6f4320f8333e2eb66cb76d2b83efe56");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_airfield.ff", "f535e4819d431d9bae9a47be26ac2fbfd51c0db81282d0c0e84edfb9cf8eeafb");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_airfield_load.ff", "65bbe16dcfd3028911f08d8b7390bcb2500b0328fff6e5dc8dbf6133a8155521");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_asylum.ff", "7b338947c088cd5ac6f93b4ceeb1ae27e2d98b20e6c4806c150818317caf7664");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_asylum_load.ff", "a64e03a98b9374a9b4c1ff47686c30c34ee217f2cc1a8816f0d1d8ec81da468e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_bgate.ff", "ca9fda6ef8ca14eca4baacb852491eb78bf1e68c6725cf588e93c936c7de6baa");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_bgate_load.ff", "dddc9d91a65cacbe63747cb55651062c7e0e68c12323d326e4aa3c865013e89b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_castle.ff", "ca45b22a5e1a555b9217fbe27228608e7495966b4c45592ddae151bccd63bf43");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_castle_load.ff", "e795e669c91885870b62273d5eaa82693a919ffbc0b31bc0dafd409f511433a4");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_courtyard.ff", "725c36778fe1f9e65ea10a2377862de4fd1e38a2c9fa887fe62dcf705b6d4143");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_courtyard_load.ff", "c6a4c8e7730fae1f898a0418211018a1c888a8af6b77f3be3ea6c371b9817a94");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_docks.ff", "f47b900fac1e6c1bab227feea5d81a3403a28f016265c7266e83df74cb6fa062");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_docks_load.ff", "1c90f6112b6d9621d871e4c411fcf9d0ba206d6667a9b40cc7e9b37ed26d38c4");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_dome.ff", "882b8bb046b200862152f5921a06c47d9c887b5124144106ff2909f0cd5ac369");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_dome_load.ff", "fa162155dd17944245450ecb0e62fe3bd49e562c120b6e089a95543215a1a057");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_downfall.ff", "8f678af09c1ffcd4ebab92ee895529b47669b78f97dbe7dfdb1da7aa4baffb19");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_downfall_load.ff", "9f7d25ecef45ab7bc31210eba6b82728e5fa1f57fdec7c9627d4d1846af58fb1");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_drum.ff", "462738ebbc99ffbc5dc39f1dd02b3dc1b82a12d1736a8121ef9e34d1b92493a1");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_drum_load.ff", "2adb5bbfc7c9d473f6d23690b57ccdbda30f758bdb15081dd6b5e74453b73e0a");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_hangar.ff", "01fb5fce6b52497d03de0e57643d9005c245295644c011f8b02f1b0cd0aafff2");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_hangar_load.ff", "90137926f56c2d5ff4ac7a291561429c856a66b582c245f4aa10da384f86044e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_kneedeep.ff", "14d845cebd9a93467bbdbeeb8662a92755ad09d0e75121d358e2b5a88151fe22");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_kneedeep_load.ff", "cffafd2b42484d2c5ea7189da286f978e6ac2eb24d4b8aa7fe530ac776d72222");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_kwai.ff", "a967f24855c17011207f09dfc3aa4698cd425a49573352d653eef9d3b6860e83");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_kwai_load.ff", "a49f172f9b3b65b117dbda6ba0679d69e00e70b5b09f9498724b861b34ae600c");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_makin_day.ff", "5e2ba1f4084d2459534cd3afe9207b7b59f580379b8093f217f9116978efa41b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_makin_day_load.ff", "0ef75dde847cef1991021bdf99c1f2e93b2d85185a468cb19cd1e757ddc68199");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_makin.ff", "e512915eb3d37bed5f502c4f9999cb3e08eb1ba3785c77e61f155af4d0f69932");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_makin_load.ff", "57242801f7425b3167ae4db9e96a97adda69399f00cba05d16e8a67e1815fe51");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_nachtfeuer.ff", "fd5ad11c9aba6ba74ca4efd02391170798f4a054bb9805bdb656747a70049c9b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_nachtfeuer_load.ff", "8d062cdfbe57092bbbd34df5e826371d8a275888b6d76e9eec66a081719ff36c");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_outskirts.ff", "3292c85b2521a85655d68b0573b276146a4492ffd1056c1c392ecd2e5a1ce3b9");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_outskirts_load.ff", "75b34fb82af7716e8d528f9e05defda2000de79cb4cd32d05cc4108e501442d2");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_roundhouse.ff", "b7114f427657145e80316750e4527392bf7bb5e18063241d51c7972812a4c40b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_roundhouse_load.ff", "74ff581cdae9f7bb9b982510675c75c556018885583e11f883d48feaef88ca2f");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_seelow.ff", "12ae3a86b5939ed0957b6da214c6ed1dd9de9c800920df119c50422e6a9231d5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_seelow_load.ff", "54cea2ab75f6786f34aef5a72d4b3ecda1f89e77b906b021e158249de4ce92a4");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_shrine.ff", "d75534b6c22334636c5b525e2571c9bd5398a78346244c80a54d6533e76410f4");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_shrine_load.ff", "590bdf0774ff4707c90804970adaa8a363e5b487d70545b4397e25683a30f442");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_stalingrad.ff", "2d88faec464a49f533b720f8ed7a6ed4d4abc0649b9ebf2fb1ce12dc758617f9");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_stalingrad_load.ff", "1ea51e89954696f38df0c981a20b51e5fbcb0ac32056e86ca35bc89c2c2a0f04");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_suburban.ff", "02daa316a24bcbc08da0fec076427b4219bd99b8a6ace2c480618e79a18be8b5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_suburban_load.ff", "d8c798f4464ecdffe328a771dc4bebd93c608aabfad722d36d6f6b7cfcfe273b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_subway.ff", "3d533dc138d54a218ddded9900352ca1f090e70081a6b5a85ee0a30c1c6caf31");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_subway_load.ff", "ff2ebb60d2dededa267eea4ece871edc93517daf71556f0124aa14abb8b2760f");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_vodka.ff", "5363259786823d3e8e5ea1a781ebeee839451b5fe570142d5a37f51f3f6625bd");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/mp_vodka_load.ff", "551fc00b2fc3fdf40eec8c1f4401ea42ae92d105dfc2e66fc8677bed8378a97e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_asylum.ff", "62707f92222217f3998c98a0a1681323260a2b0be50697c275669200a9b4ad65");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_asylum_load.ff", "98e0822b8a6f4dd2b7da8a11c537b807e37fb929edaadd149758cfbe90115d02");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_asylum_patch.ff", "c4c1df754bb2625a69e866c1d6ccc90cc903bbde2993b02c52e67f817e738f87");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_factory.ff", "7c6286743adbf7431a7945798559be3622adb03c99e36f64fe557f6163afe902");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_factory_load.ff", "8c1c2ca633dd72ed3a139605375b646ba50d9a94811c408061ba6ce431e62559");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_factory_patch.ff", "a97ce02875eb97ed04f1a3df1b40c59065ddd2a567f1cf01c8b4413c6cd5e1f7");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_prototype.ff", "38115356797fe80a539cc9ec880c621b309bafc3df2845a7a5122eec4286b9ee");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_prototype_load.ff", "07ab164dcb63eda0652a1855fc937375f290642c0f82e4c94bfdfdeaf537b0ff");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_sumpf.ff", "062e5fefd0f86f96e64f0abfc9a4507c4844043f08ac21eced85c2fb20fce0db");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_sumpf_load.ff", "28e4fd594caaac30b3bb37c1b9ba047aa97ee65b7bf2899de3a4f06ab361afbf");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/nazi_zombie_sumpf_patch.ff", "6cc3c4454698c426d9bb70f14ae1ff5f7ecc5dcf171d87a712769ad4efe304e5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/oki2.ff", "0f6cf7a372f33fae0230f9f38fe4f246258b85f15e7d4aa093c4d83379112ed0");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/oki2_load.ff", "4b5f0f609359f40b692718d56000d3cf5a08911bda44e9109916c6141b515f5e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/oki3.ff", "03a63efe70edd155ec1ef687c229e7a9d86c1779d2c13fd6e6dae5a7a21b8022");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/oki3_load.ff", "da09342edcf640e1e6bdc51b55aedbbf889a161a2f744c91c3d8ae7eaeb8208b");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/outro.ff", "1d5562cb79b04a4c10ac8b97c049932125ac63fd6cb498c1bc222bde4d4c9e2c");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/patch.ff", "3021b86b8b1b1b0c9c2f7f2e7123f1a6ce3f8af1be83b5f6f8a25f4c602f19ca");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/patch_mp.ff", "c282efa039044774567da841eae89142c33e9189ba840639c22077b5b55e0870");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pby_fly.ff", "605cf4df6850b186d0587cec8dfed29b4eef0f51e67d02e6fc9678653ad1dd72");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pby_fly_load.ff", "4d28c26fa034a88739f5be74863e9b83b922470685cd46bd73a98e92959f4d73");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1a.ff", "06ece117bbbfb59ab72e8f50f6e7fd53388f6122e8d79b163492b516a854a4f5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1a_load.ff", "8a568e60b5ea1e8779e74e7a328afdada38705e82fbb273bddaa0db4735ab0ff");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1b.ff", "0754e9ab4b3b8cff477f206a9d67405fafa2fbf2661eec10a6dc849e24c4e7b8");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1b_load.ff", "92432c190bed08334d36c53b1e2b2069c1fd57f09f40c39d794fbf7c95ad0268");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1.ff", "3876c252d98ba2b6769cddd8ab21139d4f6fa0ed2fbe19e2d631159e51f22807");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel1_load.ff", "4d9ef15b43abadf5d90c62b3131a0976846795a0bfbb9e1ff75083e30401f09f");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel2.ff", "8c1aafebb791aadea2e3e8bd297981fa6053eeec69319f69a9b86e544cfd81a0");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/pel2_load.ff", "4983c9840e415db91a33c4c46d33a88c571352e06a427bcc543aa34867b2ea54");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/see1.ff", "33336d2e94dedc137a8f6c8c9332986b4a8bb56a6c3f8d43ce1457b880e4f614");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/see1_load.ff", "96385f77542669c7c3195234c74d4e024758270e81dbe66f3b7c82d8c8885ee3");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/see2.ff", "6540695ee6aa04b828b73e88811669a0028abd32f2174d5bcbd4ad9564c14143");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/see2_load.ff", "70fa7e1788963caf48f2009c5fdc8eff656de926e6c047cf314633254b5a54e2");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/sniper.ff", "131374d389fe2c928d46cf459748a8a9f35a8b206582a0f1b834476f2ce96cd3");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/sniper_load.ff", "ae6803004066fab6ec47b7ef914a00e031f1c34acd4aa85360b4909c51701b17");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ui.ff", "6764c1a8de96292358ccbdd7d099fa11bfe5918aaa2ac53669ce8034f5da41d7");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/ui_mp.ff", "6039baa4b631aff24d11f224f95f702e182a98334b8e4c618f452146d95a3642");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_code_post_gfx_mp.ff", "116b8bc8c79effa4a2892e6ba11ca4bc6ff55bd6ae659d15678b0bd47296b965");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_common_mp.ff", "90e38dca8aae603ca57e9de76da3038f6733bcb8e2c4b7bf5f0d6a6b4084f4b6");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_airfield.ff", "b0d9d9845892a95c564c6f5304a0310337b62f4f849aea9fc1dd9c1052cd7517");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_asylum.ff", "a0ad26d1451d8766aa6bd8faa6662625ef1b585253fbd52d1703cab6a80f4148");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_bgate.ff", "fb3343a5f0d633bc24a3b802218e25974e925a05d8d16b2b940bd10d61ea4f32");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_castle.ff", "a7ad53bf478c6e99c5bfe7fdc077ef576bcb2ca834c3c818de8268a64a4aae55");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_courtyard.ff", "d457fcc7fdc188d9ad54fe6839d02f4c41cb29a8a717fa5b2a475f712c4118d8");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_docks.ff", "b4d510693ca86dadf0c54998d154e18b5285e81bd6fa80536f5adbf1c40227bf");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_dome.ff", "dba916fb8b7be3e3a05a284739be7f32d9636b64d64b8ee0919b9af1721640f5");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_downfall.ff", "20c9409b44068981d976ca3a76cac4ae9f9592495bf11d5d2fefa177ba1715fc");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_drum.ff", "28ea08e61a3188b1691867130f9f2be9cb34b894d7ee9ff03db1168bf6a321cc");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_hangar.ff", "c80ace945a07d99277fe73adf77dd6f30362176977579e0aa5156cf02cc7169a");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_kneedeep.ff", "d6bc1ca95b84dc76c486ba6753fd5ec8c92978a5a32b3e9bd76750ee1b06457e");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_kwai.ff", "2db4fb2f2406ff180cf79c78ce4ceb2474853b6bad8e85f7a6519034b6bcccba");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_makin_day.ff", "5efd66a19e18f9e7aacff75c19d4bbf44927d91fc24e1ef3116c84b731d9dbbf");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_makin.ff", "57ac613d332b4fef00889ddfca169d019c8213f447fa515d5172cf1838ee9b19");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_nachtfeuer.ff", "2b88b10ced49996514f30f6b112e49696ca183b833c5f1bd9307f0e5116608bc");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_outskirts.ff", "adc3dedf1b3ff93eeb82191dd56517a2aff168ecdc58d97dfce92fa7fe554420");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_roundhouse.ff", "139ae6e65f34f6df9a3b44b81873bf0d95aa2d3c723f5006913564678ed22d76");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_seelow.ff", "be1bf482342a38d618d3358445ab8abe6479cb24a77f84fa9a1e54a32d370f08");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_shrine.ff", "3c64d8cca3b8ce69e4693c34b2cf35d1074465e0ce66f80c77aa59cbca4c1c87");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_stalingrad.ff", "4c788481b5f63951f701898c52e00bbf3bdbd27b935675fb02f9b713c941a260");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_suburban.ff", "2c31db21336270b09154671b7f841ffe59044f9f057465d76d635e9a09b821e1");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_subway.ff", "4f8c693d4c7833d7383a1c86ec924dc7e789bef232292dd38cd8ce43a6baafb0");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_mp_vodka.ff", "b3e8dc54b1a7e932570b6110e745e63b01a5c0eac53fd6e736d1f8fb5b06ee32");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_nazi_zombie_asylum.ff", "47859f504883405b260563320a21a4baf527de4765488705ce8abbb5e55ab419");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_nazi_zombie_factory.ff", "a7c1e4a0408ede0dae9216d4d3b346bd6835105d7f7861856ba7ad2822c05318");
        persistGameFileHash(GAME_CODWAW, "./zone/italian/localized_nazi_zombie_sumpf.ff", "140471d4de5077bd759da9b5a0fae1d25f2814e52e0f08ce642707e647e0e453");
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

    public void checkSettings() {
        persistSetting(SettingConstant.AUTOSTART_LOG_PARSING, false);
        persistSetting(SettingConstant.AUTOSTART_SERVER_RUNNER, false);
        persistSetting(SettingConstant.AUTOSTART_SPECTATOR_RUNNER, false);
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

    private void persistGameFileHash(String gameTag, String fileName, String hashValue) {
        Game game = gameRepository.findByTag(gameTag);
        GameFile gameFile = gameFileRepository.findByGameAndFilename(game, fileName);
        if (gameFile == null) {
            gameFile = new GameFile(game, fileName);
            GameFileHash gameFileHash = new GameFileHash(gameFile, FileHash.SHA256, hashValue);
            gameFile.getGameFileHashes().add(gameFileHash);
            gameFileRepository.save(gameFile);
        }
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

    private void persistSetting(String param, Object value) {
        Setting setting = settingRepository.findByParam(param);
        if (setting == null) {
            setting = new Setting(param);
            setting.setValue(String.valueOf(value));
            settingRepository.save(setting);
        }
    }
}
