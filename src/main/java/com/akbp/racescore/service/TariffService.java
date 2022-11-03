package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.entity.dictionary.CarClass;
import com.akbp.racescore.model.repository.StageScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final StageScoreRepository stageScoreRepository;

    public void calculateStageTariffes(Event event, Stage stage) {
        List<StageScore> scoresToTariff = stageScoreRepository.findByStageIdAndPenaltyIsNotNull(stage.getStageId());
        if (scoresToTariff.isEmpty()) return;

        List<EventTeam> eventTeams = event.getEventTeams();
        List<EventClasses> eventClasses = event.getEventClasses().stream().sorted(Comparator.comparing(x -> x.getMaxEngineCapacity())).collect(Collectors.toList());
        HashMap<CarClass, Long> tariffByClass = getTariffByClass(stage, eventClasses);

        for (StageScore score : scoresToTariff) {
            Optional<EventTeam> eventTeam = eventTeams.stream().filter(x -> x.getTeamId().equals(score.getTeamId())).findFirst();
            if (eventTeam.isEmpty())
                continue;

            int eventClassIndex = 0;
            for (EventClasses ec : eventClasses) {
                if (ec.getCarClassId() == eventTeam.get().getCarClassId())
                    break;
                eventClassIndex++;
            }

            int tempIndex = eventClassIndex;
            while (tempIndex > -1 && tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0)
                tempIndex--;

            if (tempIndex == -1 || tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0) {
                tempIndex = eventClassIndex;
                while (eventClasses.size() > tempIndex && tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0)
                    tempIndex++;
            }

            Long tariff = tariffByClass.get(eventClasses.get(tempIndex).getCarClass());
            if (tariff == 0)
                tariff = (long) (1.5 * stageScoreRepository.findByStageIdAndDisqualifiedFalseAndPenaltyIsNullAndTeamIdIn(stage.getStageId(), eventTeams.stream().map(x -> x.getTeamId()).collect(Collectors.toList())).stream().mapToLong(x -> x.getScore()).min().orElse(0L));

            score.setScore(tariff);
            stageScoreRepository.save(score);
        }
    }

    private HashMap<CarClass, Long> getTariffByClass( Stage stage, List<EventClasses> eventClasses) {
        HashMap<CarClass, Long> scoresByClass = new HashMap<>();

        List<StageScoreSumDTO> stageScores = stageScoreRepository.findScoresInStage(stage.getStageId());

        for (EventClasses eventClass : eventClasses.stream().sorted(Comparator.comparingDouble(EventClasses::getMaxEngineCapacity)).collect(Collectors.toList())) {
            List<StageScoreSumDTO> stageScores2 = stageScores.stream().filter(x->x.getCarClass().equals(eventClass.getCarClass().getName())).collect(Collectors.toList());
            Long minScore = stageScores2.stream().mapToLong(x -> (x.getSumScore() + x.getPenalty()*1000)).min().orElse(0L);
            scoresByClass.put(eventClass.getCarClass(), (long) (minScore * 1.5));
        }

        updateIfSlowerThanTariff(stageScores, scoresByClass, stage);

        return scoresByClass;
    }

    private void updateIfSlowerThanTariff(List<StageScoreSumDTO> stageScores, HashMap<CarClass, Long> scoresByClass, Stage stage) {
        for (CarClass carClass : scoresByClass.keySet()) {
            List<StageScoreSumDTO> toHighScores = stageScores.stream()
                    .filter(x->x.getCarClass().equals(carClass.getName()))
                    .filter(x->(x.getSumScore() + x.getPenalty()*1000) > scoresByClass.get(carClass))
                    .collect(Collectors.toList());
            for (StageScoreSumDTO highScore : toHighScores){
                stageScoreRepository.findByStageIdAndTeamNumber(stage.getStageId(),  highScore.getNumber())
                        .stream().peek(x->x.setScore(scoresByClass.get(carClass))).forEach(stageScoreRepository::save);
            }
        }
    }
}
