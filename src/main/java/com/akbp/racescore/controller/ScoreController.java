package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
    public Long addScore(@RequestBody ScoreDTO score, Authentication auth) {
        return scoreService.addScore(score, auth);
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
}
