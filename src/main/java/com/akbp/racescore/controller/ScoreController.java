package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.TeamOption;
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

    @GetMapping("/getTeamOptions")
    public List<TeamOption> getTeamOptions(@RequestParam("stageId") Long stageId) {
        List<TeamOption> teamOptions = scoreService.getTeamOptions(stageId);
        return teamOptions;
    }

    @GetMapping("getStageScores")
    public List<StageScoreDTO> getStageScores(@RequestParam("stageId") Long stageId) {
        return scoreService.getStageScores(stageId);
    }

    @GetMapping("getStagesSumScores")
    public List<StageScoreDTO> getStagesSumScores(@RequestParam("stageId") Long stageId) {
        return scoreService.getStagesSumScores(stageId);
    }
}
