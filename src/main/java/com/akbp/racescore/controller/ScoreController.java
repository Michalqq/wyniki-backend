package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.PenaltyByTeamDTO;
import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.TeamOption;
import com.akbp.racescore.model.entity.Penalty;
import com.akbp.racescore.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/score", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/addScore")
    public Long addScore(@RequestBody ScoreDTO score) {
        return scoreService.addScore(score);
    }

    @PostMapping("/addPenalty")
    public Long addPenalty(@RequestBody Penalty penalty) {
        return scoreService.addPenalty(penalty);
    }

    @GetMapping("/getTeamOptions")
    public List<TeamOption> getTeamOptions(@RequestParam("stageId") Long stageId, @RequestParam("mode") String mode) {
        List<TeamOption> teamOptions = scoreService.getTeamOptions(stageId, mode);
        return teamOptions;
    }

    @GetMapping("/getTeamScore")
    public StageScoreDTO getTeamScore(@RequestParam("stageId") Long stageId, @RequestParam("teamId") Long teamId) {
        return scoreService.getTeamScore(stageId, teamId);
    }

    @GetMapping("getStageScores")
    public List<StageScoreDTO> getStageScores(@RequestParam("stageId") Long stageId) {
        return scoreService.getStageScores(stageId);
    }

    @GetMapping("getStagesSumScores")
    public List<StageScoreDTO> getStagesSumScores(@RequestParam("eventId") Long eventId, @RequestParam("stageId") Long stageId) {
        return scoreService.getStagesSumScores(eventId, stageId);
    }

    @GetMapping("getPenalties")
    public List<PenaltyByTeamDTO> getPenalties(@RequestParam("eventId") Long eventId) {
        return scoreService.getPenalties(eventId);
    }

    @PostMapping("removePenalty")
    public boolean removePenalty(@RequestParam("penaltyId") Long penaltyId)
    {
        return scoreService.removePenalty(penaltyId);
    }
}
