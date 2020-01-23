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
}
