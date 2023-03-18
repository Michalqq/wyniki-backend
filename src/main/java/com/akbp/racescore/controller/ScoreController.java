package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreController.class);

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/addScore")
    public String addScore(@RequestBody ScoreDTO score, Authentication auth) {
        try {
            return scoreService.addScore(score, auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/removeScore")
    public String removeScore(@RequestBody ScoreDTO score, Authentication auth) {
        try {
            return scoreService.removeScore(score, auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/calculateTariff")
    public void calculateTariff(@RequestParam Long eventId, Authentication auth) {
        try {
            scoreService.calculateTariff(eventId, auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @GetMapping("/getTeamScore")
    public StageScoreDTO getTeamScore(@RequestParam("eventId") Long eventId, @RequestParam("stageId") Long stageId, @RequestParam("teamId") Long teamId) {
        return scoreService.getTeamScore(eventId, stageId, teamId);
    }

    @GetMapping("getStageScores")
    public List<StageScoreDTO> getStageScores(@RequestParam("stageId") Long stageId) {
        return scoreService.getStageScores(stageId);
    }

    @GetMapping("getStagesSumScores")
    public List<StageScoreDTO> getStagesSumScores(@RequestParam("eventId") Long eventId, @RequestParam("stageId") Long stageId) {
        return scoreService.getStagesSumScores(eventId, stageId);
    }

    @GetMapping("getCompareScores")
    public List<StageScore> getCompareScores(@RequestParam("eventId") Long eventId, @RequestParam("numbers") List<Long> numbers) {
        return scoreService.getCompareScores(eventId, numbers);
    }
}
