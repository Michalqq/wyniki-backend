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
        HashMap<CarClass, Long> tariffByClass = getTariffByClass(event, stage, eventClasses);

        for (StageScore score : scoresToTariff) {
            Optional<EventTeam> eventTeam = eventTeams.stream().filter(x -> x.getTeamId().equals(score.getTeamId())).findFirst();
            if (eventTeam.isEmpty())
                continue;

//            CarClass eventClass = eventTeams.stream().filter(x -> x.getTeamId().equals(score.getTeamId())).findFirst().get().getCarClass();
            int eventClassIndex = 0;
            for (EventClasses ec : eventClasses) {
                if (ec.getCarClassId() == eventTeam.get().getCarClassId())
                    break;
                eventClassIndex++;
            }

//            List<EventTeam> eventTeamsByClassWith3Driver = new ArrayList<>();
            int tempIndex = eventClassIndex;
            while (tempIndex > -1 && tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0) {
                tempIndex--;
                if (tempIndex > -1) {
                    int finalTempIndex = tempIndex;
//                    eventTeamsByClassWith3Driver = eventTeams.stream().filter(x -> x.getCarClassId() == eventClasses.get(finalTempIndex).getCarClassId()).collect(Collectors.toList());
                }
            }
            if (tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0) {
                tempIndex = eventClassIndex;
                while (eventClasses.size() > tempIndex && tariffByClass.get(eventClasses.get(tempIndex).getCarClass()) == 0) {
                    tempIndex++;
                    if (eventClasses.size() > tempIndex) {
                        int finalTempIndex = tempIndex;
//                        eventTeamsByClassWith3Driver = eventTeams.stream().filter(x -> x.getCarClassId() == eventClasses.get(finalTempIndex).getCarClassId()).collect(Collectors.toList());
                    }
                }
            }
            Long tariff = tariffByClass.get(eventClasses.get(tempIndex).getCarClass());
            if (tariff == 0)
                tariff = (long) (1.5 * stageScoreRepository.findByStageIdAndDisqualifiedFalseAndPenaltyIsNullAndTeamIdIn(stage.getStageId(), eventTeams.stream().map(x -> x.getTeamId()).collect(Collectors.toList())).stream().mapToLong(x -> x.getScore()).min().orElse(0L));
//            else
//                tariff = tariffByClass.get(eventTeamsByClassWith3Driver.get(0).getCarClass());

            score.setScore(tariff);
            stageScoreRepository.save(score);
        }
    }

    private HashMap<CarClass, Long> getTariffByClass(Event event, Stage stage, List<EventClasses> eventClasses) {
        HashMap<CarClass, Long> scoresByClass = new HashMap<>();

        List<StageScoreSumDTO> stageScores = stageScoreRepository.findScoresInStage(stage.getStageId());

        for (EventClasses eventClass : eventClasses.stream().sorted(Comparator.comparingDouble(EventClasses::getMaxEngineCapacity)).collect(Collectors.toList())) {
//            List<EventTeam> eventTeams = event.getEventTeams().stream().filter(x -> x.getCarClassId() == eventClass.getCarClassId()).collect(Collectors.toList());
            List<StageScoreSumDTO> stageScores2 = stageScores.stream().filter(x->x.getCarClass().equals(eventClass.getCarClass().getName())).collect(Collectors.toList());


//            List<StageScore> scores = stageScoreRepository.findByStageIdAndDisqualifiedFalseAndPenaltyIsNullAndTeamIdIn(stage.getStageId(), eventTeams.stream().map(x -> x.getTeamId()).collect(Collectors.toList()));
            Long minScore = stageScores2.size()>2 ? stageScores2.stream().mapToLong(x -> (x.getSumScore() + x.getPenalty()*1000)).min().orElse(0L) : 0L;
            scoresByClass.put(eventClass.getCarClass(), (long) (minScore * 1.5));
        }

        return scoresByClass;
    }

}
