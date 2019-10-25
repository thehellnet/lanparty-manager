package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.service.ShowcaseService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/showcase")
public class ShowcaseController {

    private final ShowcaseService showcaseService;

    public ShowcaseController(ShowcaseService showcaseService) {
        this.showcaseService = showcaseService;
    }

    @RequestMapping(
            path = "{tag}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(@PathVariable(value = "tag") String showcaseTag) {
        JSONObject repsonse = showcaseService.prepareShowcase(showcaseTag);
        return ResponseEntity.ok(repsonse.toString());
    }
}
