package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.exception.cfg.InvalidDataCfgException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.exception.seat.SeatNotFoundException;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.tool.EmptyToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.tool.barcode.BarcodeToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.tool.barcode.SaveCfgToolRequestDTO;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.service.CfgService;
import org.thehellnet.lanparty.manager.service.SeatService;
import org.thehellnet.utility.StringUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(path = "/api/v1/tool")
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);

    private final SeatService seatService;
    private final CfgService cfgService;

    public ToolController(SeatService seatService, CfgService cfgService) {
        this.seatService = seatService;
        this.cfgService = cfgService;
    }

    @RequestMapping(
            path = "/ping",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse ping(@RequestBody EmptyToolRequestDTO dto) {
        return JsonResponse.getInstance();
    }

    @RequestMapping(
            path = "/welcome",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse welcome(HttpServletRequest request, @RequestBody EmptyToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("Welcome from tool at {}", remoteAddress);

        Seat seat = seatService.findByAddress(remoteAddress);
        if (seat == null) {
            logger.warn("Seat not found");
            return JsonResponse.getErrorInstance("Seat not found");
        }

        seatService.updateLastContact(seat);

        return JsonResponse.getInstance("name", seat.getName());
    }

    @RequestMapping(
            path = "/getCfg",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse getCfg(HttpServletRequest request, @RequestBody BarcodeToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("getCfg from tool at {} with barcode {}", remoteAddress, dto.getBarcode());

        String cfgContent = "";

        try {
            cfgContent = cfgService.getCfgFromRemoteAddressAndBarcode(remoteAddress, dto.getBarcode());
        } catch (SeatNotFoundException | InvalidDataCfgException | PlayerNotFoundException e) {
            logger.warn(e.getMessage());
            return JsonResponse.getErrorInstance(e.getMessage());
        }

        List<String> cfgLines = StringUtility.splitLines(cfgContent);
        return JsonResponse.getInstance(cfgLines);
    }

    @RequestMapping(
            path = "/saveCfg",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse saveCfg(HttpServletRequest request, @RequestBody SaveCfgToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("saveCfg from tool at {} with barcode {} and {} lines in cfg", remoteAddress, dto.getBarcode(), dto.getCfgLines().size());

        return JsonResponse.getInstance();
    }
}
