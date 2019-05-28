package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.tool.PingToolRequestDTO;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.service.SeatService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/api/v1/public/tool")
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);
    private final SeatService seatService;

    public ToolController(SeatService seatService) {
        this.seatService = seatService;
    }

    @RequestMapping(
            path = "/ping",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse ping(@RequestBody PingToolRequestDTO dto, HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("Ping from tool at {}", remoteAddress);

        Seat seat = seatService.findByAddress(remoteAddress);

        if (seat == null) {
            logger.warn("Seat not found");
            return JsonResponse.getErrorInstance("Seat not found");
        }

        seatService.updateLastContact(seat);

        return JsonResponse.getInstance("name", seat.getName());
    }
}
