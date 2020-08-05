package org.thehellnet.lanparty.manager.utility.logline

import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException
import spock.lang.Specification
import spock.lang.Unroll

class AbstractLogLineParserTest extends Specification {

    @Unroll
    def "parseTime: \"#input\" - Exception thrown"(String input) {
        when:
        AbstractLogLineParser.parseTime(input)

        then:
        thrown LogLineParserException

        where:
        input | _
        null  | _
        ""    | _
        " "   | _
        "123" | _
        ":"   | _
        "1:"  | _
        ":1"  | _
    }

    @Unroll
    def "parseTime: \"#input\" - #expected"(String input, int expected) {
        when:
        int actual = AbstractLogLineParser.parseTime(input)

        then:
        actual == expected

        where:
        input  | expected
        "0:0"  | 0
        "0:1"  | 1
        "0:10" | 10
        "0:59" | 59
        "1:00" | 60
        "1:01" | 61
        "1:59" | 119
        "2:00" | 120
    }

    @Unroll
    def "splitItems: \"#input\" - Exception thrown"(String input) {
        when:
        AbstractLogLineParser.splitItems(input)

        then:
        thrown LogLineParserException

        where:
        input | _
        null  | _
        ""    | _
        " "   | _
    }

    @Unroll
    def "splitItems: \"#input\" - #expected"(String input, String[] expected) {
        when:
        String[] actual = AbstractLogLineParser.splitItems(input)

        then:
        actual == expected

        where:
        input                                                                                                                              | expected
        "a"                                                                                                                                | ["a"]
        ";"                                                                                                                                | ["", ""]
        "a;"                                                                                                                               | ["a", ""]
        ";a"                                                                                                                               | ["", "a"]
        "a;a"                                                                                                                              | ["a", "a"]
        "K;4ae423d8025cecb67a2decac0e7cbcd2;3;;Cosmo;5e33cd23356ac8c7301a465351fda263;4;;Sandro;m1014_mp;19;MOD_PISTOL_BULLET;torso_upper" | ["K", "4ae423d8025cecb67a2decac0e7cbcd2", "3", "", "Cosmo", "5e33cd23356ac8c7301a465351fda263", "4", "", "Sandro", "m1014_mp", "19", "MOD_PISTOL_BULLET", "torso_upper"]
    }
}
