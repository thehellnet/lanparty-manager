package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        JSONArray jsonArray = MetadataUtility.getInstance().compute();
        return ResponseEntity.ok(jsonArray.toString());
    }
}
