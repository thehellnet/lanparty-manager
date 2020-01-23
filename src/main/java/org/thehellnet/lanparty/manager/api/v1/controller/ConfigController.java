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

    @RequestMapping(
            path = "/metadata",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity metadata() {
        JSONArray responseBody = MetadataUtility.getInstance().compute();
        return ResponseEntity.ok(responseBody.toString());
    }

    @RequestMapping(
            path = "/metadata/{entityName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity metadata(@RequestParam(name = "entityName") String entityName) {
        String className = entityName.substring(0, entityName.length() - 1);
        className = className.substring(0, 1).toUpperCase() + className.substring(1);

        MetadataUtility metadataUtility = MetadataUtility.getInstance();
        Class<?> entityClass = metadataUtility.search(className);
        if (entityClass == null) {
            throw new NotFoundException(String.format("Class %s not found using name %s", className, entityName));
        }

        JSONObject responseBody = metadataUtility.computeClass(entityClass);
        return ResponseEntity.ok(responseBody.toString());
    }
}
