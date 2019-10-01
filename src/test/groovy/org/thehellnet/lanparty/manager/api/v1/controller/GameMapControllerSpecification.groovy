package org.thehellnet.lanparty.manager.api.v1.controller

class GameMapControllerSpecification extends ControllerSpecification {

    def setup() {
        'Do login for token retrieving'()
    }

//    def list() {
//        given:
//        def requestBody = new JSONObject([
//                "token"  : token,
//                "gameTag": "cod4"
//        ])
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/gameMap/list")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//        response.has("success")
//        response.getBoolean("success")
//
//        response.has("data")
//        JSONArray data = response.getJSONArray("data")
//        data.length() > 0
//
//        data.toList().each { item ->
//            JSONObject gameJson = (JSONObject) item
//            gameJson.has("tag")
//            ((String) gameJson.get("tag")).length() > 0
//            gameJson.has("name")
//            ((String) gameJson.get("name")).length() > 0
//            gameJson.has("gameTag")
//            ((String) gameJson.get("gameTag")) == "cod4"
//            gameJson.has("stock")
//            ((boolean) gameJson.get("gameTag"))
//        }
//    }
}
