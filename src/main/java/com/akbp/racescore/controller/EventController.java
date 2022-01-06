package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.ClassesOption;
import com.akbp.racescore.model.dto.PsOption;
import com.akbp.racescore.model.dto.StgesAndClassesDTO;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class EventController {

    private static final String GENERAL = "GENERALNA";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/getAll")
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @PostMapping("/startEvent")
    public boolean startEvent(@RequestParam("eventId") Long eventId) {
        return eventService.startEvent(eventId);
    }

    @GetMapping("/getStages")
    public List<String> getStages(@RequestParam("eventId") Long eventId) {
        List<String> stages = eventService.getStages(eventId);
        return stages;
    }

    @GetMapping("/getTeams")
    public List<Team> getTeams(@RequestParam("eventId") Long eventId) {
        List<Team> teams = eventService.getTeams(eventId);
        return teams;
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
}
