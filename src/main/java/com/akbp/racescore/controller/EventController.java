package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.EventDTO;
import com.akbp.racescore.model.dto.EventTeamDto;
import com.akbp.racescore.model.dto.EventWithLogoDTO;
import com.akbp.racescore.model.dto.FileDto;
import com.akbp.racescore.model.dto.StgesAndClassesDTO;
import com.akbp.racescore.model.dto.selectors.ClassesOption;
import com.akbp.racescore.model.dto.selectors.PsOption;
import com.akbp.racescore.model.dto.selectors.RefereeOption;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/event", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/getAll")
    public List<EventDTO> getAll(Authentication auth) {
        try {
            return eventService.getAll(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getAllFuture")
    public List<EventDTO> getAllFuture(Authentication auth) {
        try {
            return eventService.getAllFuture(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getAllBefore")
    public List<EventDTO> getAllBefore(Authentication auth) {
        try {
            return eventService.getAllBefore(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @PostMapping("/startEvent")
    public boolean startEvent(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.startEvent(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @GetMapping("/getStages")
    public List<String> getStages(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getStages(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getDriverCount")
    public Long getDriverCount(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getDriverCount(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return 0L;
    }

    @GetMapping("/getTeams")
    public List<EventTeam> getTeams(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getTeams(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getBasicTeams")
    public List<EventTeamDto> getBasicTeams(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getBasicTeams(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getPsOptions")
    public List<PsOption> getPsOptions(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getPsOptions(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @GetMapping("/getStagesAndClasses")
    public StgesAndClassesDTO getStagesAndClasses(@RequestParam("eventId") Long eventId) {
        try {
            List<PsOption> psOptions = eventService.getPsOptions(eventId);
            List<ClassesOption> classes = eventService.getClasses(eventId);
            StgesAndClassesDTO sacDTO = new StgesAndClassesDTO(psOptions, classes);
            return sacDTO;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;

    }

    @PostMapping("/removeTeam")
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

    @PostMapping("saveEventTeam")
    public void saveEventTeam(@RequestParam("eventId") Long eventId, @RequestBody Team team) {
        try {
            eventService.saveEventTeam(eventId, team);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @PutMapping("createNew")
    public Long createNew(@RequestBody Event event) {
        Long respone = 0L;
        try {
            respone = eventService.createNew(event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return respone;
    }

    @PostMapping("addLogoFile")
    public boolean addLogoFile(@RequestBody MultipartFile file,
                               @RequestParam("eventId") Long eventId) {
        try {
            eventService.addLogoFile(file, eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    @PostMapping("addFileToEvent")
    public boolean addFileToEvent(@RequestBody MultipartFile file,
                                  @RequestParam("fileName") String fileName,
                                  @RequestParam("desc") String desc,
                                  @RequestParam("eventId") Long eventId) {
        if (file == null)
            return false;

        try {
            eventService.addFileToEvent(file, eventId, fileName, desc);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    @PostMapping("removeFileFromEvent")
    public boolean removeFileFromEvent(@RequestParam("fileId") Long fileId,
                                       @RequestParam("eventId") Long eventId) {
        try {
            return eventService.removeFile(fileId, eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @GetMapping("getEvent")
    public EventWithLogoDTO getEvent(@RequestParam Long eventId) {
        try {
            return eventService.getEvent(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @GetMapping("/getEventFile")
    public ResponseEntity<byte[]> getEventFile(@RequestParam("id") Long fileId) {
        try {
            return eventService.getEventFile(fileId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PostMapping("teamChecked")
    public Boolean teamChecked(@RequestParam Long eventId, @RequestParam Long teamId, @RequestParam boolean checked) {
        try {
            return eventService.teamChecked(eventId, teamId, checked);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @PostMapping("bkChecked")
    public Boolean bkChecked(@RequestParam Long eventId, @RequestParam Long teamId, @RequestParam boolean checked) {
        try {
            return eventService.bkChecked(eventId, teamId, checked);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    @GetMapping("/fetchCreateFinalList")
    public boolean fetchCreateFinalList(@RequestParam("eventId") Long eventId,
                                        @RequestParam("stageId") Long stageId,
                                        @RequestParam("pkc") String pkc,
                                        @RequestParam("startTime") Instant startTime,
                                        @RequestParam("frequency") Long frequency,
                                        Authentication auth) {
        try {
            return eventService.fetchCreateFinalList(auth, eventId, stageId, pkc, startTime, frequency);
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

    @GetMapping("/getEntryFeeFile")
    public ResponseEntity<byte[]> getEntryFeeFile(@RequestParam("eventId") Long eventId,
                                                  @RequestParam("teamId") Long teamId) {
        try {
            return eventService.getFile(eventId, teamId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/checkReferee")
    public boolean checkReferee(@RequestParam("eventId") Long eventId, Authentication auth) {
        return eventService.checkReferee(eventId, auth);
    }

    @GetMapping("/getLogoPath")
    public FileDto getLogoPath(@RequestParam("eventId") Long eventId) {
        try {
            return eventService.getLogoPath(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    @GetMapping("/getRefereeOptions")
    public List<RefereeOption> getRefereeOptions() {
        return eventService.getRefereeOptions();
    }

    @GetMapping("/getEventClassesOptions")
    public List<ClassesOption> getEventClassesOptions() {
        return eventService.getEventClassesOptions();
    }

    @GetMapping("/getAllEventClassesOptions")
    public List<ClassesOption> getAllEventClassesOptions(@RequestParam("eventId") Long eventId) {
        return eventService.getAllEventClassesOptions(eventId);
    }

    @PostMapping("/sortByClass")
    public List<EventTeam> sortByClass(@RequestBody List<EventTeam> teams) {
        return eventService.sortByClass(teams);
    }

    @GetMapping("/getManualCarClass")
    public String getManualCarClass(@RequestParam("eventId") Long eventId,
                                    @RequestParam("teamId") Long teamId) {
        return eventService.getManualCarClass(eventId, teamId);
    }

    @PostMapping("/saveManualCarClass")
    public void saveManualCarClass(@RequestParam("eventId") Long eventId,
                                   @RequestParam("teamId") Long teamId,
                                   @RequestParam("carClassId") Long carClassId) {
        eventService.saveManualCarClass(eventId, teamId, carClassId);
    }

    @PostMapping("/saveNumbersAndClasses")
    public boolean saveNumbersAndClasses(@RequestBody List<EventTeam> teams, @RequestParam("eventId") Long eventId) {
        try {
            return eventService.saveNumbersAndClasses(teams, eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return false;
    }
}
