package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.ClassesOption;
import com.akbp.racescore.model.dto.PsOption;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.EventTeamRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final String GENERAL = "GENERALNA";

    private final EventRepository eventRepository;
    private final EventTeamRepository eventTeamRepository;
    private final StageScoreRepository stageScoreRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        EventTeamRepository eventTeamRepository,
                        StageScoreRepository stageScoreRepository) {
        this.eventRepository = eventRepository;
        this.eventTeamRepository = eventTeamRepository;
        this.stageScoreRepository = stageScoreRepository;

    }

    public List<String> getStages(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.get().getStages().stream()
                .sorted(Comparator.comparingLong(Stage::getStageId))
                .map(x -> x.getName())
                .collect(Collectors.toList());
    }

    public List<Team> getTeams(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.get().getEventTeams().stream()
                .sorted(Comparator.comparingLong(EventTeam::getNumber))
                .map(x -> x.getTeam()).collect(Collectors.toList());
    }

    public List<PsOption> getPsOptions(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.get().getStages().stream()
                .sorted(Comparator.comparingLong(Stage::getStageId))
                .map(x -> new PsOption(x.getName(), x.getStageId().toString(), false)).collect(Collectors.toList());
    }

    public boolean startEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Event event = eventOptional.get();

        for (Stage stage : event.getStages())
            for (EventTeam team : event.getEventTeams())
                createEmptyEventScore(stage, team);

        return true;
    }

    private void createEmptyEventScore(Stage stage, EventTeam team) {
        StageScore stageScore = new StageScore();
        stageScore.setStageId(stage.getStageId());
        stageScore.setTeam(team.getTeam());
        stageScore.setTeamNumber(team.getNumber());

        stageScoreRepository.save(stageScore);
    }

    public List<Event> getAll() {
        List<Event> events = eventRepository.findAll();
        return events;
    }

    public List<ClassesOption> getClasses(Long eventId) {
        List<ClassesOption> classesOptions = new ArrayList<>();
        classesOptions.add(new ClassesOption(GENERAL, "0", true));
        List<String> test = eventRepository.findDistinctClasses(eventId);

        classesOptions.addAll(
                eventRepository.findDistinctClasses(eventId).stream()
                        .sorted().collect(Collectors.toList()).stream()
                        .map(x -> new ClassesOption(x, x, false)).collect(Collectors.toList()));

        return classesOptions;
    }

    public String addTeamToEvent(Team team, Long eventId) {
        try {
            int number = eventTeamRepository.getMaxNumberByEventId(eventId);

            EventTeam et = new EventTeam();
            et.setTeam(team);
            et.setEventId(eventId);
            et.setNumber(number + 1);
            eventTeamRepository.save(et);
            return "Załoga została dodana do wydarzenia";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
