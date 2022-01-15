package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.selectors.TeamOption;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.StageScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final EventService eventService;
    private final StageScoreRepository stageScoreRepository;

    @Autowired
    public TeamService(EventService eventService,
                       StageScoreRepository stageScoreRepository) {
        this.eventService = eventService;
        this.stageScoreRepository = stageScoreRepository;
    }

    public List<TeamOption> getTeamOptions(Long stageId, String mode) {
        List<StageScore> scores = new ArrayList<>();
        if (mode.equals("NEW"))
            scores = stageScoreRepository.findByStageIdAndScoreIsNullAndDisqualifiedFalse(stageId);
        else if (mode.equals("EDIT"))
            scores = stageScoreRepository.findByStageIdAndScoreIsNotNullAndDisqualifiedFalse(stageId);
        else if (mode.equals("PENALTY"))
            scores = stageScoreRepository.findByStageIdAndDisqualifiedFalse(stageId);

        return scores.stream()
                .sorted(Comparator.comparingLong(StageScore::getTeamNumber))
                .map(x -> new TeamOption(x.getTeamNumber() + " - " + x.getTeam().getDriver(), x.getTeam().getTeamId().toString(), false)).collect(Collectors.toList());
    }

    public String addTeam(Team team, Long eventId) {
        try {
            eventService.addTeamToEvent(team, eventId);

            return "Załoga została utworzona";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
