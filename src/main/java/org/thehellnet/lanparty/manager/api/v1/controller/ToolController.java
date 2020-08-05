package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.model.dto.request.tool.BarcodeToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.tool.SaveCfgToolRequestDTO;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.service.CfgService;
import org.thehellnet.lanparty.manager.service.SeatService;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandSerializer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/public/v1/tool",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);

    private final SeatService seatService;
    private final CfgService cfgService;

    public ToolController(SeatService seatService, CfgService cfgService) {
        this.seatService = seatService;
        this.cfgService = cfgService;
    }

    @PostMapping(path = "/ping")
    public ResponseEntity<Object> ping(HttpServletRequest request) {
        logger.info("Ping from tool at {}", request.getRemoteAddr());
        return ResponseEntity.ok(new Object());
    }

    @PostMapping(path = "/welcome")
    public ResponseEntity<Object> welcome(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("Welcome from tool at {}", remoteAddress);

        seatService.updateLastContact(remoteAddress);
        return ResponseEntity.ok(new Object());
    }

    @PostMapping(path = "/getCfg")
    public ResponseEntity<List<String>> getCfg(HttpServletRequest request, @RequestBody BarcodeToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("getCfg from tool at {} with barcode {}", remoteAddress, dto.barcode);

        seatService.updateLastContact(remoteAddress);
        seatService.updatePlayerInSeats(remoteAddress, dto.barcode);

        List<ParsedCfgCommand> cfgCommands = cfgService.computeCfg(remoteAddress, dto.barcode);
        List<String> cfgLines = new ParsedCfgCommandSerializer(cfgCommands).serializeLines();
        return ResponseEntity.ok(cfgLines);
    }

    @PostMapping(path = "/saveCfg")
    public ResponseEntity<Object> saveCfg(HttpServletRequest request, @RequestBody SaveCfgToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("saveCfg from tool at {} with barcode {} and {} lines in cfg", remoteAddress, dto.barcode, dto.cfgLines.size());

        cfgService.saveCfg(remoteAddress, dto.barcode, dto.cfgLines);
        return ResponseEntity.ok(new Object());
    }
}
