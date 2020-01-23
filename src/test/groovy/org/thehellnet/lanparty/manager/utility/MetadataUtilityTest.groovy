package org.thehellnet.lanparty.manager.utility

import org.json.JSONArray
import spock.lang.Specification

class MetadataUtilityTest extends Specification {

    def "Compute metadata"() {
        given:
        MetadataUtility metadataUtility = MetadataUtility.getInstance()

        when:
        JSONArray jsonArray = metadataUtility.compute()

        then:
        println jsonArray.toString()
    }
}
