package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.exception.cfg.CfgException;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.tool.GetCfgToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.tool.PingToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tool.GetCfgToolResponseDTO;
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
    public JsonResponse ping(HttpServletRequest request, @RequestBody PingToolRequestDTO dto) {
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

//    @RequestMapping(
//            path = "/getCfg",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public JsonResponse getCfg(@RequestBody GetCfgToolRequestDTO dto, HttpServletRequest request) {
//        String remoteAddress = request.getRemoteAddr();
//        logger.info("getCfg from tool at {}", remoteAddress);
//
//        String cfgContent;
//
//        try {
//            cfgContent = cfgService.getCfgContentFromRemoteAddressAndBarcode(remoteAddress, dto.getBarcode());
//        } catch (CfgException e) {
//            logger.warn(e.getMessage());
//            return JsonResponse.getErrorInstance(e.getMessage());
//        }
//
//        List<String> cfgLines = StringUtility.splitLines(cfgContent);
//        GetCfgToolResponseDTO responseDTO = new GetCfgToolResponseDTO(cfgLines);
//        return JsonResponse.getInstance(responseDTO);
//    }
}
