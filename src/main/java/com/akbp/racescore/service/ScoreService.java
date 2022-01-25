package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Stage;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.repository.EventTeamRepository;
import com.akbp.racescore.model.repository.StageRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import com.akbp.racescore.utils.ScoreToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private final StageRepository stageRepository;
    private final StageScoreRepository stageScoreRepository;
    private final UserRepository userRepository;
    private final EventTeamRepository eventTeamRepository;

    @Autowired
    public ScoreService(StageScoreRepository stageScoreRepository,
                        StageRepository stageRepository,
                        UserRepository userRepository,
                        EventTeamRepository eventTeamRepository) {
        this.stageScoreRepository = stageScoreRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
        this.eventTeamRepository = eventTeamRepository;
    }

    public Long addScore(ScoreDTO score, Authentication auth) {
        Stage stage = stageRepository.findByStageId(score.getStageId());
        Set<StageScore> stageScores = stage.getStageScores();

        StageScore stageScore = stageScores.stream()
                .filter(x -> x.getTeam().getTeamId() == score.getTeamId())
                .filter(y -> y.getStageId() == score.getStageId())
                .findFirst().get();
        stageScore.setScore(score.getScore());
        setUserMod(stageScore, auth);
        stageScore.setDateMod(Instant.now());
        stageScoreRepository.save(stageScore);

        return 1L;
    }

    private void setUserMod(StageScore stageScore, Authentication auth) {
        if (auth == null)
            return;

        User user = userRepository.findByUsername(auth.getName());
        if (user == null)
            return;

        stageScore.setUserMod(user.getUserId());
    }

    public List<StageScoreDTO> getStageScores(Long stageId) {
        List<StageScoreSumDTO> scoresDTOS = stageScoreRepository.findScoresInStage(stageId);
        return calculateTime(scoresDTOS);
    }

    public List<StageScoreDTO> getStagesSumScores(Long eventId, Long stageId) {
        List<StageScoreSumDTO> scoresDTOS = stageScoreRepository.findSummedScoreByStageId(eventId, stageId);
        return calculateTime(scoresDTOS);
    }

    private List<StageScoreDTO> calculateTime(List<StageScoreSumDTO> scoresDTOS) {
        List<StageScoreDTO> scores = scoresDTOS.stream().map(x -> new StageScoreDTO(x)).collect(Collectors.toList());
        scores = scores.stream().sorted(Comparator.comparingLong(x -> x.getTotalTimeWithPenalty())).collect(Collectors.toList());
        if (scores.isEmpty())
            return Collections.emptyList();

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

    public StageScoreDTO getTeamScore(Long eventId, Long stageId, Long teamId) {
        List<StageScore> stageScores = stageScoreRepository.findByStageIdAndTeamId(stageId, teamId);
        stageScores.sort(Comparator.comparingLong(x -> x.getScore()));
        if (stageScores.size() > 1)
            stageScores.subList(1, stageScores.size()).forEach(x -> stageScoreRepository.deleteById(x.getId()));

        StageScore stageScore = stageScores.get(0);
        EventTeam et = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);

        StageScoreDTO stageScoreDTO = new StageScoreDTO(stageScore, et.getCarClass().getName());
        stageScoreDTO.setScoreFromTotalScore(stageScore);
        return stageScoreDTO;
    }

}
