package com.akbp.racescore.service;

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

            CarClass eventClass = eventTeams.stream().filter(x -> x.getTeamId().equals(score.getTeamId())).findFirst().get().getCarClass();
            int eventClassIndex = 0;
            for (EventClasses ec : eventClasses) {
                if (ec.getCarClassId() == eventClass.getCarClassId())
                    break;
                eventClassIndex++;
            }

            List<EventTeam> eventTeamsByClassWith3Driver = new ArrayList<>();
            int tempIndex = eventClassIndex;
            while (eventTeamsByClassWith3Driver.size() < 2 && tempIndex > -1) {
                tempIndex--;
                if (tempIndex > -1) {
                    int finalTempIndex = tempIndex;
                    eventTeamsByClassWith3Driver = eventTeams.stream().filter(x -> x.getCarClassId() == eventClasses.get(finalTempIndex).getCarClassId()).collect(Collectors.toList());
                }
            }
            if (eventTeamsByClassWith3Driver.isEmpty()) {
                tempIndex = eventClassIndex;
                while (eventTeamsByClassWith3Driver.size() < 2 && eventClasses.size() >= tempIndex) {
                    tempIndex++;
                    if (eventClasses.size() >= tempIndex) {
                        int finalTempIndex = tempIndex;
                        eventTeamsByClassWith3Driver = eventTeams.stream().filter(x -> x.getCarClassId() == eventClasses.get(finalTempIndex).getCarClassId()).collect(Collectors.toList());
                    }
                }
            }

            if (eventTeamsByClassWith3Driver.isEmpty()) return;

            Long tariff = tariffByClass.get(eventTeamsByClassWith3Driver.get(0).getCarClass());
            score.setScore(tariff);
            stageScoreRepository.save(score);
        }

    }

    private HashMap<CarClass, Long> getTariffByClass(Event event, Stage stage, List<EventClasses> eventClasses) {
        HashMap<CarClass, Long> scoresByClass = new HashMap<>();

        for (EventClasses eventClass : eventClasses) {
            List<EventTeam> eventTeams = event.getEventTeams().stream().filter(x -> x.getCarClassId() == eventClass.getCarClassId()).collect(Collectors.toList());
            List<StageScore> scores = stageScoreRepository.findByStageIdAndDisqualifiedFalseAndPenaltyIsNullAndTeamIdIn(stage.getStageId(), eventTeams.stream().map(x -> x.getTeamId()).collect(Collectors.toList()));
            Long minScore = scores.stream().mapToLong(x -> x.getScore()).min().orElse(0L);
            scoresByClass.put(eventClass.getCarClass(), (long) (minScore * 1.5));
        }

        return scoresByClass;
    }

}
