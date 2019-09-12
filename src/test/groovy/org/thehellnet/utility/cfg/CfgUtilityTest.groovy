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
                " test   \n test "                : new CfgCommand("test", "test"),
                " test   \n test\n "              : new CfgCommand("test", "test"),
                "test\nTEST\ntest"                : new CfgCommand("testTESTtest"),
                " test   \n test\r "              : new CfgCommand("test", "test"),
                "test\nTEST\rtest"                : new CfgCommand("testTESTtest"),
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
                "  test   TEST   test tesssst  er": new CfgCommand("test", "TEST", "test tesssst er")
        ]

        expect:
        assert CfgUtility.parseCommand(null) == null

        DATA.each {
            if (CfgUtility.parseCommand(it.key) != it.value) {
                assert false
            }
        }
    }
}
