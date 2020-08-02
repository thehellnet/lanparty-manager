package org.thehellnet.lanparty.manager.utility

import org.json.JSONArray
import org.json.JSONObject
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import spock.lang.Specification
import spock.lang.Unroll

class MetadataUtilityTest extends Specification {

    def "Compute all"() {
        given:
        MetadataUtility metadataUtility = MetadataUtility.getInstance()

        when:
        JSONArray jsonArray = metadataUtility.compute()

        then:
        println jsonArray.toString()
    }

    def "Compute single class"() {
        given:
        MetadataUtility metadataUtility = MetadataUtility.getInstance()

        when:
        JSONObject jsonObject = metadataUtility.computeClass(AppUser.class)

        then:
        println jsonObject.toString()
    }

    @Unroll
    def "Compute Class title: #input -> #expected"(String input, String expected) {
        when:
        String actual = MetadataUtility.computeTitle(input)

        then:
        actual == expected

        where:
        input        | expected
        null         | null
        ""           | ""
        "a"          | "A"
        "A"          | "A"
        "Aa"         | "Aa"
        "AA"         | "A A"
        "aA"         | "A A"
        "simple"     | "Simple"
        "Simple"     | "Simple"
        "Simplename" | "Simplename"
        "SimpleName" | "Simple Name"
        "simpleName" | "Simple Name"
    }

    @Unroll
    def "Similar method: #inputClassName - #inputName -> #expected"(String inputClassName, String inputName, String expected) {
        when:
        String actual = MetadataUtility.similar(inputClassName, inputName)

        then:
        actual == expected

        where:
        inputClassName | inputName  | expected
        null           | null       | false
        null           | ""         | false
        ""             | null       | false
        ""             | ""         | false
        "a"            | ""         | false
        "ab"           | ""         | false
        "abc"          | ""         | false
        "a"            | "a"        | true
        "ab"           | "a"        | false
        "abc"          | "a"        | false
        "a"            | "ab"       | true
        "ab"           | "ab"       | true
        "abc"          | "ab"       | false
        "a"            | "abc"      | true
        "ab"           | "abc"      | true
        "abc"          | "abc"      | true
        "A"            | ""         | false
        "Ab"           | ""         | false
        "Abc"          | ""         | false
        "A"            | "a"        | true
        "Ab"           | "a"        | false
        "Abc"          | "a"        | false
        "A"            | "ab"       | true
        "Ab"           | "ab"       | true
        "Abc"          | "ab"       | false
        "A"            | "abc"      | true
        "Ab"           | "abc"      | true
        "Abc"          | "abc"      | true
        "a"            | "A"        | true
        "ab"           | "A"        | false
        "abc"          | "A"        | false
        "a"            | "Ab"       | true
        "ab"           | "Ab"       | true
        "abc"          | "Ab"       | false
        "a"            | "Abc"      | true
        "ab"           | "Abc"      | true
        "abc"          | "Abc"      | true
        "A"            | ""         | false
        "Ab"           | ""         | false
        "Abc"          | ""         | false
        "A"            | "A"        | true
        "Ab"           | "A"        | false
        "Abc"          | "A"        | false
        "A"            | "Ab"       | true
        "Ab"           | "Ab"       | true
        "Abc"          | "Ab"       | false
        "A"            | "Abc"      | true
        "Ab"           | "Abc"      | true
        "Abc"          | "Abc"      | true
        "Match"        | "matches"  | true
    }
}
