package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.EventDTO;
import com.akbp.racescore.model.dto.StgesAndClassesDTO;
import com.akbp.racescore.model.dto.selectors.ClassesOption;
import com.akbp.racescore.model.dto.selectors.PsOption;
import com.akbp.racescore.model.dto.selectors.RefereeOption;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/event", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private static final String GENERAL = "GENERALNA";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/getAll")
    public List<EventDTO> getAll(Authentication auth) {
        return eventService.getAll(auth);
    }

    @PostMapping("/startEvent")
    public boolean startEvent(@RequestParam("eventId") Long eventId) {
        boolean respone = false;
        try {
            respone = eventService.startEvent(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return respone;
    }

    @GetMapping("/getStages")
    public List<String> getStages(@RequestParam("eventId") Long eventId) {
        List<String> stages = eventService.getStages(eventId);
        return stages;
    }

    @GetMapping("/getTeams")
    public List<EventTeam> getTeams(@RequestParam("eventId") Long eventId) {
        return eventService.getTeams(eventId);
    }

    @GetMapping("/getPsOptions")
    public List<PsOption> getPsOptions(@RequestParam("eventId") Long eventId) {
        return eventService.getPsOptions(eventId);
    }

    @GetMapping("/getStagesAndClasses")
    public StgesAndClassesDTO getStagesAndClasses(@RequestParam("eventId") Long eventId) {
        List<PsOption> psOptions = eventService.getPsOptions(eventId);
        List<ClassesOption> classes = eventService.getClasses(eventId);
        StgesAndClassesDTO sacDTO = new StgesAndClassesDTO(psOptions, classes);
        return sacDTO;
    }

    @PostMapping("removeTeam")
    public boolean removeTeam(@RequestParam("eventId") Long eventId, @RequestParam("teamId") Long teamId) {
        try {
            eventService.removeTeam(eventId, teamId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return true;
    }

    @PostMapping("confirmEntryFee")
    public boolean confirmEntryFee(@RequestParam("eventId") Long eventId, @RequestParam("teamId") Long teamId) {
        try {
            eventService.confirmEntryFee(eventId, teamId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return true;
    }

    @PutMapping("createNew")
    public boolean createNew(@RequestBody Event event) {
        boolean respone = false;
        try {
            respone = eventService.createNew(event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return respone;
    }

    @GetMapping("getEvent")
    public EventDTO getEvent(@RequestParam Long eventId) {
        try {
            return eventService.getEvent(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @PostMapping("deleteEvent")
    public Boolean deleteEvent(@RequestParam Long eventId) {
        try {
            return eventService.deleteEvent(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @PostMapping(value = "addEntryFeeFile")
    public boolean addEntryFeeFile(@RequestBody MultipartFile file,
                                   @RequestParam("eventId") Long eventId,
                                   @RequestParam("teamId") Long teamId) {
        try {
            return eventService.saveFile(file, eventId, teamId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @GetMapping("/checkReferee")
    public boolean checkReferee(@RequestParam("eventId") Long eventId, Authentication auth) {
        return eventService.checkReferee(eventId, auth);
    }

    @GetMapping("/getRefereeOptions")
    public List<RefereeOption> getRefereeOptions() {
        return eventService.getRefereeOptions();
    }
}
