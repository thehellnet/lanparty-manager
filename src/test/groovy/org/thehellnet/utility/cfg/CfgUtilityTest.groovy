package org.thehellnet.utility.cfg

import spock.lang.Specification

class CfgUtilityTest extends Specification {

    def "parseCommand"() {
        given:
        def DATA = [
                ""                                : null,
                " "                               : null,
                "  "                              : null,
                "\n"                              : null,
                " \n"                             : null,
                "\n "                             : null,
                " \n "                            : null,
                "\n\n"                            : null,
                "\n \n"                           : null,
                " \n \n"                          : null,
                " \n \n "                         : null,
                " \n  \n "                        : null,
                "\n\r"                            : null,
                "\n \r"                           : null,
                " \n \r"                          : null,
                " \n \r "                         : null,
                " \n  \r "                        : null,
                "test"                            : new CfgCommand("test"),
                " test"                           : new CfgCommand("test"),
                "test "                           : new CfgCommand("test"),
                " test "                          : new CfgCommand("test"),
                "test test"                       : new CfgCommand("test", "test"),
                "test test "                      : new CfgCommand("test", "test"),
                " test test"                      : new CfgCommand("test", "test"),
                " test test "                     : new CfgCommand("test", "test"),
                " test    test "                  : new CfgCommand("test", "test"),
                " test   \n test "                : null,
                " test   \n test\n "              : null,
                "test\nTEST\ntest"                : null,
                " test   \n test\r "              : null,
                "test\nTEST\rtest"                : null,
                "test TEST test"                  : new CfgCommand("test", "TEST", "test"),
                "test   TEST test"                : new CfgCommand("test", "TEST", "test"),
                "test TEST   test"                : new CfgCommand("test", "TEST", "test"),
                "test   TEST   test"              : new CfgCommand("test", "TEST", "test"),
                "test   TEST   test  "            : new CfgCommand("test", "TEST", "test"),
                "  test   TEST   test  "          : new CfgCommand("test", "TEST", "test"),
                "  test   TEST   test"            : new CfgCommand("test", "TEST", "test"),
                "test TEST test tesssst"          : new CfgCommand("test", "TEST", "test tesssst"),
                "test TEST test   tesssst"        : new CfgCommand("test", "TEST", "test tesssst"),
                "test TEST test   tesssst "       : new CfgCommand("test", "TEST", "test tesssst"),
                "test TEST   test     tesssst"    : new CfgCommand("test", "TEST", "test tesssst"),
                "test   TEST   test tesssst"      : new CfgCommand("test", "TEST", "test tesssst"),
                "  test   TEST   test tesssst er" : new CfgCommand("test", "TEST", "test tesssst er"),
                "  test   TEST   test tesssst  er": new CfgCommand("test", "TEST", "test tesssst er"),
                "test TEST \"test tesssst  er\""  : new CfgCommand("test", "TEST", "test tesssst er")
        ]

        expect:
        assert CfgUtility.parseCommand(null) == null

        DATA.each {
            if (CfgUtility.parseCommand(it.key) != it.value) {
                assert false
            }
        }
    }

    def "sanitize with null input"() {
        given:
        String tournamentCfg = null
        String playerCfg = null
        String expected = ""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with null tournament input"() {
        given:
        String tournamentCfg = null
        String playerCfg = ""
        String expected = ""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with null player input"() {
        given:
        String tournamentCfg = ""
        String playerCfg = null
        String expected = ""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with empty input"() {
        given:
        String tournamentCfg = ""
        String playerCfg = ""
        String expected = ""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with empty tournamentCfg input"() {
        given:
        String tournamentCfg = ""
        String playerCfg = "// generated by Call of Duty, do not modify\n" +
                "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String expected = ""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with empty playerCfg input"() {
        given:
        String tournamentCfg = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String playerCfg = ""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with replace of params contained in playerCfg"() {
        given:
        String tournamentCfg = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String playerCfg = "bind 4 \"+gostand\"\n" +
                "bind H \"+smoke\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+gostand\"\n" +
                "bind H \"+smoke\""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }

    def "sanitize with replace of params contained in playerCfg and extra config params"() {
        given:
        String tournamentCfg = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String playerCfg = "bind 4 \"+gostand\"\n" +
                "bind Z \"+test\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+gostand\"\n" +
                "bind H \"say Google\""

        when:
        String actual = CfgUtility.sanitize(tournamentCfg, playerCfg)

        then:
        actual == expected
    }
}
