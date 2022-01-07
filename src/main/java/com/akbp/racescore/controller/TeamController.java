package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.TeamOption;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/team", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/addTeam")
    public String addTeam(@RequestParam("eventId") Long eventId, @RequestBody Team team) {
        return teamService.addTeam(team, eventId);
    }

    @GetMapping("/getTeamOptions")
    public List<TeamOption> getTeamOptions(@RequestParam("stageId") Long stageId, @RequestParam("mode") String mode) {
        List<TeamOption> teamOptions = teamService.getTeamOptions(stageId, mode);
        return teamOptions;
    }
}
