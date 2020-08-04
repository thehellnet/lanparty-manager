package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.utility.MetadataUtility;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/config")
public class ConfigController {

    @GetMapping(
            path = "/metadata",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> metadata() {
        JSONArray responseBody = MetadataUtility.newInstance().compute();
        return ResponseEntity.ok(responseBody.toString());
    }

    @GetMapping(
            path = "/metadata/{entityName}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> metadata(@PathVariable(name = "entityName") String entityName) {
        MetadataUtility metadataUtility = MetadataUtility.newInstance();
        Class<?> entityClass = metadataUtility.search(entityName);
        if (entityClass == null) {
            throw new NotFoundException(String.format("Class not found using name %s", entityName));
        }

        JSONObject responseBody = metadataUtility.computeClass(entityClass);
        return ResponseEntity.ok(responseBody.toString());
    }
}
