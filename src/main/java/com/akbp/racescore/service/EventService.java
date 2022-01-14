package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.ClassesOption;
import com.akbp.racescore.model.dto.EventDTO;
import com.akbp.racescore.model.dto.PsOption;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.EventTeamRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    public List<EventTeam> getTeams(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.get().getEventTeams().stream().sorted(Comparator.comparingInt(x -> x.getNumber())).collect(Collectors.toList());
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
        stageScore.setDisqualified(false);

        stageScoreRepository.save(stageScore);
    }

    public List<EventDTO> getAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(x -> new EventDTO(x)).collect(Collectors.toList());
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
            et.setJoinDate(Instant.now());
            et.setTeam(team);
            et.setEventId(eventId);
            et.setNumber(number + 1);
            eventTeamRepository.save(et);
            return "Załoga została dodana do wydarzenia";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Transactional
    public void removeTeam(Long eventId, Long teamId) {
        try {
            stageScoreRepository.removeStageScoresByTeamIdAndEventId(eventId, teamId);
            eventTeamRepository.deleteByEventIdAndTeamId(eventId, teamId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void confirmEntryFee(Long eventId, Long teamId) {
        EventTeam eventTeam = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);
        eventTeam.setEntryFeePaid(true);
        eventTeamRepository.save(eventTeam);
    }

    public void createNew(Event event) {
        eventRepository.save(event);
    }

    public boolean checkReferee(Long eventId, Authentication auth) {
        if (auth == null)
            return false;

        Optional<Event> optionalEvent = eventRepository.checkIfUserIsReferee(eventId, auth.getName());
        return optionalEvent.isPresent();
    }
}
