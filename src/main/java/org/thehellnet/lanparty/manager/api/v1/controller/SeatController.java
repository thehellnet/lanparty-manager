package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.seat.CreateSeatRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.seat.UpdateSeatRequestDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.service.SeatService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/seat")
public class SeatController {

    private static final Logger logger = LoggerFactory.getLogger(SeatController.class);

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateSeatRequestDTO dto) {
        Seat seat = seatService.create(dto.name, dto.ipAddress, dto.tournament, dto.player);
        return ResponseEntity.created(URI.create("")).body(seat);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Seat seat = seatService.read(id);
        return ResponseEntity.ok(seat);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Seat> seats = seatService.readAll();
        return ResponseEntity.ok(seats);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateSeatRequestDTO dto) {
        Seat seat = seatService.update(id, dto.name, dto.ipAddress, dto.tournament, dto.player);
        return ResponseEntity.ok(seat);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
