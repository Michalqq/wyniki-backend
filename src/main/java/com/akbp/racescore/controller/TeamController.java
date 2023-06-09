package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.selectors.TeamOption;
import com.akbp.racescore.model.dto.selectors.TeamOptionList;
import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/team", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class TeamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/getTeam")
    public Team getTeam(Authentication auth) {
        try {
            return teamService.getTeam(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/getTeamByTeamId")
    public Team getTeamByTeamId(@RequestParam("teamId") Long teamId) {
        try {
            return teamService.getTeamByTeamId(teamId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/addTeam")
    public Long addTeam(@RequestParam("eventId") Long eventId, @RequestBody Team team) {
        try {
            return teamService.addTeam(team, eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1L;
        }
    }

    @PostMapping("/saveTeam")
    public void saveTeam(@RequestBody Team team) {
        try {
            teamService.saveTeam(team);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @GetMapping("/getTeamOptions")
    public List<TeamOption> getTeamOptions(@RequestParam("eventId") Long eventId, @RequestParam("stageId") Long stageId, @RequestParam("mode") String mode) {
        List<TeamOption> teamOptions = teamService.getTeamOptions(eventId, stageId, mode);
        return teamOptions;
    }

    @GetMapping("/getCar")
    public Car getCar(@RequestParam("carId") Long carId) {
        try {
            return teamService.getCar(carId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/addCar")
    public String addCar(@RequestParam("teamId") Long teamId, @RequestBody Car car) {
        try {
            return teamService.addCar(teamId, car);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/getTeamOptionList")
    public TeamOptionList getTeamOptionList() {
        return teamService.getTeamOptionList();
    }
}
