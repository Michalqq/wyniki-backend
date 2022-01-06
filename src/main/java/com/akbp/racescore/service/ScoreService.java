package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.dto.TeamOption;
import com.akbp.racescore.model.entity.Penalty;
import com.akbp.racescore.model.entity.Stage;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.repository.PenaltyRepository;
import com.akbp.racescore.model.repository.StageRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.utils.ScoreToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private final StageScoreRepository stageScoreRepository;
    private final StageRepository stageRepository;
    private final PenaltyRepository penaltyRepository;

    @Autowired
    public ScoreService(StageScoreRepository stageScoreRepository,
                        StageRepository stageRepository,
                        PenaltyRepository penaltyRepository) {
        this.stageScoreRepository = stageScoreRepository;
        this.stageRepository = stageRepository;
        this.penaltyRepository = penaltyRepository;
    }

    public Long addScore(ScoreDTO score) {
        Stage stage = stageRepository.findByStageId(score.getStageId());
        Set<StageScore> stageScores = stage.getStageScores();

        StageScore stageScore = stageScores.stream()
                .filter(x -> x.getTeam().getTeamId() == score.getTeamId())
                .filter(y -> y.getStageId() == score.getStageId())
                .findFirst().get();
        stageScore.setScore(score.getScore());
        stageScore.setId(score.getStageScoreId());
        stageScoreRepository.save(stageScore);

        return 1L;
    }

    public List<TeamOption> getTeamOptions(Long stageId, String mode) {
        List<StageScore> scores = new ArrayList<>();
        if (mode.equals("NEW"))
            scores = stageScoreRepository.findByStageIdAndScoreIsNull(stageId);
        else if (mode.equals("EDIT")) scores = stageScoreRepository.findByStageIdAndScoreIsNotNull(stageId);

        return scores.stream()
                .sorted(Comparator.comparingLong(StageScore::getTeamNumber))
                .map(x -> new TeamOption(x.getTeamNumber() + " - " + x.getTeam().getDriver(), x.getTeam().getTeamId().toString(), false)).collect(Collectors.toList());
    }

    public List<StageScoreDTO> getStageScores(Long stageId) {
        List<StageScoreSumDTO> scoresDTOS = stageScoreRepository.findScoresInStage(stageId);
        return calculateTime(scoresDTOS);
    }

    public List<StageScoreDTO> getStagesSumScores(Long stageId) {
        List<StageScoreSumDTO> scoresDTOS = stageScoreRepository.findSummedScoreByStageId(stageId);
        return calculateTime(scoresDTOS);
    }

    private List<StageScoreDTO> calculateTime(List<StageScoreSumDTO> scoresDTOS) {
        List<StageScoreDTO> scores = scoresDTOS.stream().map(x -> new StageScoreDTO(x)).collect(Collectors.toList());
        scores = scores.stream().sorted(Comparator.comparingLong(x -> x.getTotalTimeWithPenalty())).collect(Collectors.toList());
        Long leadTime = scores.get(0).getTotalTimeWithPenalty();

        StageScoreDTO previousScore = null;
        int place = 1;
        for (StageScoreDTO score : scores) {
            score.setPlace(place++);

            if (previousScore != null) {
                score.setTimeTo("+" + ScoreToString.toString(score.getTotalTimeWithPenalty() - previousScore.getTotalTimeWithPenalty()));
                score.setTimeToFirst("+" + ScoreToString.toString(score.getTotalTimeWithPenalty() - leadTime));
            }
            previousScore = score;
        }

        return scores;
    }

    public StageScoreDTO getTeamScore(Long stageId, Long teamId) {
        StageScore stageScore = stageScoreRepository.findByStageIdAndTeamId(stageId, teamId);
        StageScoreDTO stageScoreDTO = new StageScoreDTO(stageScore);
        stageScoreDTO.setScoreFromTotalScore(stageScore);
        return stageScoreDTO;
    }

    public Long addPenalty(Penalty penalty) {
        penaltyRepository.save(penalty);
        return 1L;
    }
}
