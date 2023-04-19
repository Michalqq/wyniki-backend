package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.ScoreDTO;
import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.EventTeamRepository;
import com.akbp.racescore.model.repository.PenaltyRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import com.akbp.racescore.utils.ScoreToString;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final StageScoreRepository stageScoreRepository;
    private final UserRepository userRepository;
    private final EventTeamRepository eventTeamRepository;
    private final EventRepository eventRepository;
    private final TariffService tariffService;
    private final PenaltyRepository penaltyRepository;

    public String addScore(ScoreDTO score, Authentication auth) {
        List<StageScore> stageScores = stageScoreRepository.findByStageIdAndTeamId(score.getStageId(), score.getTeamId());

        if (stageScores.isEmpty())
            return null;

        StageScore stageScore = stageScores.get(0);
        saveStageScore(stageScore, score.getScore(), auth);

        return "Dodano wynik załogi: " + stageScore.getTeamNumber() + " - " + stageScore.getTeam().getDriver() + "\n"
                + "Czas: " + ScoreToString.toString(stageScore.getScore());
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

    public List<StageScoreDTO> calculateTime(List<StageScoreSumDTO> scoresDTOS) {
        List<StageScoreDTO> scores = scoresDTOS.stream().map(x -> new StageScoreDTO(x)).collect(Collectors.toList());
        scores = scores.stream().sorted(Comparator.comparingLong(x -> x.getTotalTimeWithPenalty())).collect(Collectors.toList());
        if (scores.isEmpty())
            return Collections.emptyList();

//        Long leadTime = scores.get(0).getTotalTimeWithPenalty();
//
//        StageScoreDTO previousScore = null;
//        int place = 1;
//        for (StageScoreDTO score : scores) {
//            score.setPlace(place++);
//
//            if (previousScore != null) {
//                score.setTimeTo("+" + ScoreToString.toString(score.getTotalTimeWithPenalty() - previousScore.getTotalTimeWithPenalty()));
//                score.setTimeToFirst("+" + ScoreToString.toString(score.getTotalTimeWithPenalty() - leadTime));
//            }
//            previousScore = score;
//        }

        return scores;
    }

    public StageScoreDTO getTeamScore(Long eventId, Long stageId, Long teamId) {
        List<StageScore> stageScores = stageScoreRepository.findByStageIdAndTeamId(stageId, teamId);
        stageScores.sort(Comparator.comparingLong(x -> x.getScore()));
        if (stageScores.size() > 1)
            stageScores.subList(1, stageScores.size()).forEach(x -> stageScoreRepository.deleteById(x.getId()));

        StageScore stageScore = stageScores.get(0);
        EventTeam et = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);

        StageScoreDTO stageScoreDTO = new StageScoreDTO(stageScore, et);
        stageScoreDTO.setScoreFromTotalScore(stageScore);
        return stageScoreDTO;
    }

    public String removeScore(ScoreDTO score, Authentication auth) {
        List<StageScore> stageScores = stageScoreRepository.findByStageIdAndTeamId(score.getStageId(), score.getTeamId());

        if (stageScores.isEmpty())
            return null;

        StageScore stageScore = stageScores.get(0);
        stageScore.setPenalty(null);
        saveStageScore(stageScore, null, auth);

        return "Usunięto wynik załogi: " + stageScore.getTeamNumber() + " - " + stageScore.getTeam().getDriver();
    }

    private void saveStageScore(StageScore stageScore, Long score, Authentication auth) {
        stageScore.setScore(score);
        setUserMod(stageScore, auth);
        stageScore.setDateMod(Instant.now());
        stageScoreRepository.save(stageScore);
    }

    public void calculateTariff(Long eventId, Authentication auth) {
        Event event = eventRepository.getById(eventId);

        for (Stage stage : event.getStages())
            tariffService.calculateStageTariffes(event, stage);
    }

    public List<StageScore> getCompareScores(Long eventId, List<Long> numbers) {
        var stageScores = stageScoreRepository.findAllByEventIdAndTeamNumbers(eventId, numbers);

        var penalties = penaltyRepository.findByStageIdInAndTeamIdIn(
                stageScores.stream().map(x -> x.getStageId()).collect(Collectors.toList()), stageScores.stream().map(x -> x.getTeamId()).collect(Collectors.toList()));

        stageScores.forEach(
                x -> x.setPenalty(penalties.stream().filter(penalty -> (extractPenalty(penalty, x)))
                        .mapToLong(y -> Optional.ofNullable(y.getPenaltySec()).orElse(0L)).sum()));

        return stageScores;
    }

    private boolean extractPenalty(Penalty penalty, StageScore x) {
        return penalty.getTeamId().equals(x.getTeamId()) && penalty.getStageId().equals(x.getStageId());
    }
}
