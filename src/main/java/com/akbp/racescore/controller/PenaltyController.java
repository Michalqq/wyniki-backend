package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.PenaltyByTeamDTO;
import com.akbp.racescore.model.dto.selectors.PenaltyOption;
import com.akbp.racescore.model.entity.Penalty;
import com.akbp.racescore.service.PenaltyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/penalty", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class PenaltyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreController.class);

    private PenaltyService penaltyService;

    @Autowired
    public PenaltyController(PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }

    @GetMapping("getPenaltyOptions")
    public List<PenaltyOption> getPenaltyOptions() {
        return penaltyService.getPenaltyOptions();
    }

    @GetMapping("getPenalties")
    public List<PenaltyByTeamDTO> getPenalties(@RequestParam("eventId") Long eventId) {
        return penaltyService.getPenalties(eventId);
    }

    @GetMapping("getDisqualifications")
    public List<PenaltyByTeamDTO> getDisqualifications(@RequestParam("eventId") Long eventId) {
        return penaltyService.getDisqualifications(eventId);
    }

    @PostMapping("/addPenalty")
    public Long addPenalty(@RequestBody Penalty penalty) {
        try {
            return penaltyService.addPenalty(penalty);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1L;
        }
    }

    @PostMapping("removePenalty")
    public boolean removePenalty(@RequestParam("penaltyId") Long penaltyId) {
        try {
            return penaltyService.removePenalty(penaltyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @PostMapping("removeDisqualification")
    public boolean removeDisqualification(@RequestParam("penaltyId") Long penaltyId) {
        try {
            return penaltyService.removeDisqualification(penaltyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
