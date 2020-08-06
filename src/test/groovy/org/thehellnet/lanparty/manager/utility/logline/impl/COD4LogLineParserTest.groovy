package org.thehellnet.lanparty.manager.utility.logline.impl

import org.joda.time.DateTime
import org.thehellnet.lanparty.manager.exception.InvalidDataException
import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException
import org.thehellnet.lanparty.manager.model.logline.line.*
import spock.lang.Specification
import spock.lang.Unroll

class COD4LogLineParserTest extends Specification {

    private static final String LOG_LINE_INIT_GAME = "1981:27 InitGame: \\_Admin\\[hnt]^5theory\\_Location\\Italy\\_Maps\\CoD4 Standard Maps\\_Website\\https://www.thehellnet.org\\g_compassShowEnemies\\0\\g_gametype\\dm\\gamename\\Call of Duty 4\\mapname\\mp_carentan\\protocol\\6\\shortversion\\1.7\\sv_allowAnonymous\\1\\sv_disableClientConsole\\0\\sv_floodprotect\\4\\sv_hostname\\^1The HellNet.org^7 CoD4\\sv_maxclients\\20\\sv_maxPing\\300\\sv_maxRate\\25000\\sv_minPing\\0\\sv_privateClients\\0\\sv_punkbuster\\1\\sv_pure\\1\\sv_voice\\0\\ui_maxclients\\32"
    private static final String LOG_LINE_SHUTDOWN_GAME = "2000:39 ShutdownGame:"
    private static final String LOG_LINE_JOIN = "1981:34 J;4ae423d8025cecb67a2decac0e7cbcd2;2;Cosmo"
    private static final String LOG_LINE_QUIT = "2000:16 Q;54cb46597ab3d2032c359353277c62ca;0;Gandalf"
    private static final String LOG_LINE_DAMAGE = "1982:03 D;63d91658d55799f50f45e43cf13df80c;1;allies;Rufy;;-1;world;;none;18;MOD_FALLING;none"
    private static final String LOG_LINE_KILL = "1982:12 K;63d91658d55799f50f45e43cf13df80c;1;;Rufy;54cb46597ab3d2032c359353277c62ca;0;;Gandalf;dragunov_mp;98;MOD_RIFLE_BULLET;torso_lower"
    private static final String LOG_LINE_WEAPON = "1615:51 Weapon;51f393127bd69a6317ba9c374a222cc1;1;[hnt]theory;mp5_mp"
    private static final String LOG_LINE_SAY_NORMAL = "1984:12 say;63d91658d55799f50f45e43cf13df80c;1;Rufy;^Ubela amicco"
    private static final String LOG_LINE_SAY_EXTRA_CHAR1 = "1984:31 say;4ae423d8025cecb67a2decac0e7cbcd2;2;Cosmo;^Umhmhmhmh doc'<E8>?"
    private static final String LOG_LINE_SAY_EXTRA_CHAR2 = "1984:37 say;4ae423d8025cecb67a2decac0e7cbcd2;2;Cosmo;^Unon pi<F9>"
    private static final String LOG_LINE_EXIT_LEVEL = "2000:39 ExitLevel: executed"
    private static final String LOG_LINE_SEPARATOR = "2000:39 ------------------------------------------------------------"

    @Unroll
    def "logLineParser with \"#input\" input throws LogLineParserException"(String input) {
        given:
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)

        when:
        logLineParser.parse()

        then:
        thrown LogLineParserException

        where:
        input | _
        null  | _
        ""    | _
        " "   | _
    }

    @Unroll
    def "logLineParser with \"#input\" input throws InvalidDataException"(String input) {
        given:
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)

        when:
        logLineParser.parse()

        then:
        thrown InvalidDataException

        where:
        input               | _
        LOG_LINE_EXIT_LEVEL | _
        LOG_LINE_SEPARATOR  | _
    }

    def "logLineParser with valid InitGame line"() {
        given:
        String input = LOG_LINE_INIT_GAME
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        InitGameLogLine expected = new InitGameLogLine(dateTime, 1981 * 60 + 27)
        expected.params.put("_Admin", "[hnt]^5theory")
        expected.params.put("_Location", "Italy")
        expected.params.put("_Maps", "CoD4 Standard Maps")
        expected.params.put("_Website", "https://www.thehellnet.org")
        expected.params.put("g_compassShowEnemies", "0")
        expected.params.put("g_gametype", "dm")
        expected.params.put("gamename", "Call of Duty 4")
        expected.params.put("mapname", "mp_carentan")
        expected.params.put("protocol", "6")
        expected.params.put("shortversion", "1.7")
        expected.params.put("sv_allowAnonymous", "1")
        expected.params.put("sv_disableClientConsole", "0")
        expected.params.put("sv_floodprotect", "4")
        expected.params.put("sv_hostname", "^1The HellNet.org^7 CoD4")
        expected.params.put("sv_maxclients", "20")
        expected.params.put("sv_maxPing", "300")
        expected.params.put("sv_maxRate", "25000")
        expected.params.put("sv_minPing", "0")
        expected.params.put("sv_privateClients", "0")
        expected.params.put("sv_punkbuster", "1")
        expected.params.put("sv_pure", "1")
        expected.params.put("sv_voice", "0")
        expected.params.put("ui_maxclients", "32")
        expected.gametypeTag = "dm"
        expected.mapTag = "mp_carentan"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof InitGameLogLine

        when:
        InitGameLogLine logLine = (InitGameLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid ShutdownGame line"() {
        given:
        String input = LOG_LINE_SHUTDOWN_GAME
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        ShutdownGameLogLine expected = new ShutdownGameLogLine(dateTime, 2000 * 60 + 39)

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof ShutdownGameLogLine

        when:
        ShutdownGameLogLine logLine = (ShutdownGameLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Join line"() {
        given:
        String input = LOG_LINE_JOIN
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        JoinLogLine expected = new JoinLogLine(dateTime, 1981 * 60 + 34)
        expected.guid = "4ae423d8025cecb67a2decac0e7cbcd2"
        expected.num = 2
        expected.nick = "Cosmo"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof JoinLogLine

        when:
        JoinLogLine logLine = (JoinLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Quit line"() {
        given:
        String input = LOG_LINE_QUIT
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        QuitLogLine expected = new QuitLogLine(dateTime, 2000 * 60 + 16)
        expected.guid = "54cb46597ab3d2032c359353277c62ca"
        expected.num = 0
        expected.nick = "Gandalf"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof QuitLogLine

        when:
        QuitLogLine logLine = (QuitLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Damage line"() {
        given:
        String input = LOG_LINE_DAMAGE
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        DamageLogLine expected = new DamageLogLine(dateTime, 1982 * 60 + 3)
        expected.affectedGuid = "63d91658d55799f50f45e43cf13df80c"
        expected.affectedNum = 1
        expected.affectedTeam = "allies"
        expected.affectedNick = "Rufy"
        expected.offendingGuid = ""
        expected.offendingNum = -1
        expected.offendingTeam = "world"
        expected.offendingNick = ""
        expected.weapon = "none"
        expected.damage = 18
        expected.bullet = "MOD_FALLING"
        expected.zone = "none"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof DamageLogLine

        when:
        DamageLogLine logLine = (DamageLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Kill line"() {
        given:
        String input = LOG_LINE_KILL
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        KillLogLine expected = new KillLogLine(dateTime, 1982 * 60 + 12)
        expected.affectedGuid = "63d91658d55799f50f45e43cf13df80c"
        expected.affectedNum = 1
        expected.affectedNick = "Rufy"
        expected.offendingGuid = "54cb46597ab3d2032c359353277c62ca"
        expected.offendingNum = 0
        expected.offendingNick = "Gandalf"
        expected.weapon = "dragunov_mp"
        expected.damage = 98
        expected.bullet = "MOD_RIFLE_BULLET"
        expected.zone = "torso_lower"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof KillLogLine

        when:
        KillLogLine logLine = (KillLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Weapon line"() {
        given:
        String input = LOG_LINE_WEAPON
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        WeaponLogLine expected = new WeaponLogLine(dateTime, 1615 * 60 + 51)
        expected.guid = "51f393127bd69a6317ba9c374a222cc1"
        expected.num = 1
        expected.nick = "[hnt]theory"
        expected.weapon = "mp5_mp"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof WeaponLogLine

        when:
        WeaponLogLine logLine = (WeaponLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Say normal line"() {
        given:
        String input = LOG_LINE_SAY_NORMAL
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        SayLogLine expected = new SayLogLine(dateTime, 1984 * 60 + 12)
        expected.guid = "63d91658d55799f50f45e43cf13df80c"
        expected.num = 1
        expected.nick = "Rufy"
        expected.message = "^Ubela amicco"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof SayLogLine

        when:
        SayLogLine logLine = (SayLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Say extra char line"() {
        given:
        String input = LOG_LINE_SAY_EXTRA_CHAR1
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        SayLogLine expected = new SayLogLine(dateTime, 1984 * 60 + 31)
        expected.guid = "4ae423d8025cecb67a2decac0e7cbcd2"
        expected.num = 2
        expected.nick = "Cosmo"
        expected.message = "^Umhmhmhmh doc'<E8>?"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof SayLogLine

        when:
        SayLogLine logLine = (SayLogLine) actual

        then:
        logLine == expected
    }

    def "logLineParser with valid Say another extra char line"() {
        given:
        String input = LOG_LINE_SAY_EXTRA_CHAR2
        COD4LogLineParser logLineParser = new COD4LogLineParser(input)
        DateTime dateTime = DateTime.now()

        SayLogLine expected = new SayLogLine(dateTime, 1984 * 60 + 37)
        expected.guid = "4ae423d8025cecb67a2decac0e7cbcd2"
        expected.num = 2
        expected.nick = "Cosmo"
        expected.message = "^Unon pi<F9>"

        when:
        LogLine actual = logLineParser.parse(dateTime)

        then:
        noExceptionThrown()
        actual instanceof SayLogLine

        when:
        SayLogLine logLine = (SayLogLine) actual

        then:
        logLine == expected
    }
}
