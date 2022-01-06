package com.akbp.racescore.service;

import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final EventService eventService;

    @Autowired
    public TeamService(TeamRepository teamRepository,
                       EventService eventService) {
        this.teamRepository = teamRepository;
        this.eventService = eventService;
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
