package org.thehellnet.lanparty.manager.api.v1.controller.crud;

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
import org.thehellnet.lanparty.manager.model.dto.service.SeatServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.service.crud.SeatCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/seat")
public class SeatCrudController {

    private static final Logger logger = LoggerFactory.getLogger(SeatCrudController.class);

    private final SeatCrudService seatCrudService;

    @Autowired
    public SeatCrudController(SeatCrudService seatCrudService) {
        this.seatCrudService = seatCrudService;
    }

    @CheckToken
    @CheckRoles(Role.SEAT_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateSeatRequestDTO dto) {
        SeatServiceDTO serviceDTO = new SeatServiceDTO(dto.name, dto.ipAddress, dto.tournament, dto.player);
        Seat seat = seatCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(seat);
    }

    @CheckToken
    @CheckRoles(Role.SEAT_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Seat seat = seatCrudService.read(id);
        return ResponseEntity.ok(seat);
    }

    @CheckToken
    @CheckRoles(Role.SEAT_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Seat> seats = seatCrudService.readAll();
        return ResponseEntity.ok(seats);
    }

    @CheckToken
    @CheckRoles(Role.SEAT_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateSeatRequestDTO dto) {
        SeatServiceDTO serviceDTO = new SeatServiceDTO(dto.name, dto.ipAddress, dto.tournament, dto.player);
        Seat seat = seatCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(seat);
    }

    @CheckToken
    @CheckRoles(Role.SEAT_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        seatCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
