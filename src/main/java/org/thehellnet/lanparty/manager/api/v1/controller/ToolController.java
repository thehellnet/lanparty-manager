package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.model.dto.request.tool.BarcodeToolRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.tool.SaveCfgToolRequestDTO;
import org.thehellnet.lanparty.manager.service.crud.CfgCrudService;
import org.thehellnet.lanparty.manager.service.crud.SeatCrudService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(
        method = RequestMethod.POST,
        path = "/api/public/v1/tool",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);

    private final SeatCrudService seatCrudService;
    private final CfgCrudService cfgCrudService;

    public ToolController(SeatCrudService seatCrudService, CfgCrudService cfgCrudService) {
        this.seatCrudService = seatCrudService;
        this.cfgCrudService = cfgCrudService;
    }

    @RequestMapping(path = "/ping")
    public ResponseEntity ping(HttpServletRequest request) {
        logger.info("Ping from tool at {}", request.getRemoteAddr());
        return ResponseEntity.ok(new Object());
    }

    @RequestMapping(path = "/welcome")
    public ResponseEntity welcome(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("Welcome from tool at {}", remoteAddress);

        seatCrudService.updateLastContact(remoteAddress);
        return ResponseEntity.ok(new Object());
    }

    @RequestMapping(path = "/getCfg")
    public ResponseEntity getCfg(HttpServletRequest request, @RequestBody BarcodeToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("getCfg from tool at {} with barcode {}", remoteAddress, dto.barcode);

        List<String> cfgLines = cfgCrudService.computeCfg(remoteAddress, dto.barcode);
        return ResponseEntity.ok(cfgLines);
    }

    @RequestMapping(path = "/saveCfg")
    public ResponseEntity saveCfg(HttpServletRequest request, @RequestBody SaveCfgToolRequestDTO dto) {
        String remoteAddress = request.getRemoteAddr();
        logger.info("saveCfg from tool at {} with barcode {} and {} lines in cfg", remoteAddress, dto.barcode, dto.cfgLines.size());

        cfgCrudService.saveCfg(remoteAddress, dto.barcode, dto.cfgLines);
        return ResponseEntity.ok(new Object());
    }
}
