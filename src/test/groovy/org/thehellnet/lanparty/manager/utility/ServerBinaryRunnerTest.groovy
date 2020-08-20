package org.thehellnet.lanparty.manager.utility

import spock.lang.Specification
import spock.lang.Unroll

class ServerBinaryRunnerTest extends Specification {

    @Unroll
    def "evaluateIfLineIsLoggable - \"#input\" -> #expected"(String input, boolean expected) {
        when:
        boolean actual = ServerBinaryRunner.evaluateIfLineIsLoggable(input)

        then:
        actual == expected

        where:
        input                                                                           | expected
        null                                                                            | false
        ""                                                                              | false
        " "                                                                             | false
        "CoD4 MP 1.7 build linux-i386 Jun 28 2008"                                      | false
        "begin \$init"                                                                  | false
        "----- FS_Startup -----"                                                        | false
        "Current language: english"                                                     | false
        "Current language: english"                                                     | false
        "Current search path:"                                                          | false
        "Adding channel: error"                                                         | false
        "/home/sardylan/.callofduty4/mods/pml220"                                       | false
        "    localized assets iwd file for italian"                                     | false
        "WARNING: unknown dvar 'bg_shock_volume_physics' in file 'shock/default.shock'" | false
        "WARNING: unknown dvsdfsdddsf"                                                  | true
        "Error: Missing soundalias \"weap_m249saw_turret_fire_plr\"."                   | true
    }
}
