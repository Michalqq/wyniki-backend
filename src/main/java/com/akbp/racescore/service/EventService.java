package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.EventDTO;
import com.akbp.racescore.model.dto.FileDto;
import com.akbp.racescore.model.dto.selectors.ClassesOption;
import com.akbp.racescore.model.dto.selectors.PsOption;
import com.akbp.racescore.model.dto.selectors.RefereeOption;
import com.akbp.racescore.model.dto.selectors.StageDTO;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.entity.dictionary.CarClass;
import com.akbp.racescore.model.enums.DriveType;
import com.akbp.racescore.model.repository.*;
import com.akbp.racescore.model.repository.dictionary.CarClassRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import com.akbp.racescore.service.fileGenerator.BkPdfCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final TeamRepository teamRepository;
    private final StageScoreRepository stageScoreRepository;
    private final UserRepository userRepository;
    private final CarClassRepository carClassRepository;
    private final EventPathsRepository eventPathsRepository;
    private final EventClassesRepository eventClassesRepository;
    private final PenaltyRepository penaltyRepository;
    private final EventFileRepository eventFileRepository;

    private final BkPdfCreatorService bkPdfCreatorService;

    private final CarService carService;
    private int sortIndex = 0;

    @Autowired
    public EventService(EventRepository eventRepository,
                        EventTeamRepository eventTeamRepository,
                        StageScoreRepository stageScoreRepository,
                        UserRepository userRepository,
                        TeamRepository teamRepository,
                        CarClassRepository carClassRepository,
                        CarService carService,
                        EventPathsRepository eventPathsRepository,
                        EventClassesRepository eventClassesRepository,
                        PenaltyRepository penaltyRepository,
                        EventFileRepository eventFileRepository,
                        BkPdfCreatorService bkPdfCreatorService) {
        this.eventRepository = eventRepository;
        this.eventTeamRepository = eventTeamRepository;
        this.stageScoreRepository = stageScoreRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.carClassRepository = carClassRepository;
        this.eventPathsRepository = eventPathsRepository;
        this.eventClassesRepository = eventClassesRepository;
        this.penaltyRepository = penaltyRepository;
        this.eventFileRepository = eventFileRepository;

        this.bkPdfCreatorService = bkPdfCreatorService;

        this.carService = carService;
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

        List<EventTeam> teams = eventOptional.get().getEventTeams().stream().sorted(Comparator.comparingInt(x -> x.getNumber())).collect(Collectors.toList());

        return teams;
    }

    public List<PsOption> getPsOptions(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.get().getStages().stream()
                .sorted(Comparator.comparingLong(Stage::getStageId))
                .map(x -> new PsOption(x.getName(), x.getStageId().toString(), false)).collect(Collectors.toList());
    }

    public boolean startEvent(Long eventId) {
        Event event = eventRepository.getById(eventId);
        event.setStarted(true);
        eventRepository.save(event);

        return true;
    }

    private void createEmptyEventScore(Stage stage, EventTeam team) {
        StageScore stageScore = new StageScore();
        stageScore.setStageId(stage.getStageId());
        stageScore.setTeamId(team.getTeamId());
        stageScore.setTeamNumber(team.getNumber());
        stageScore.setDisqualified(false);

        stageScoreRepository.save(stageScore);
    }

    public List<EventDTO> getAll(Authentication auth) {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOS = events.stream().map(x -> new EventDTO(x)).collect(Collectors.toList());

        if (auth != null)
            return eventWithJoinedMark(eventDTOS, auth);

        return eventDTOS;
    }

    private List<EventDTO> eventWithJoinedMark(List<EventDTO> eventDTOS, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName());
        if (user == null)
            return eventDTOS;

        Team team = teamRepository.findByUserId(user.getUserId());
        if (team == null)
            return eventDTOS;

        for (EventDTO eventDTO : eventDTOS) {
            EventTeam et = eventTeamRepository.findByEventIdAndTeamId(eventDTO.getEventId(), team.getTeamId());
            eventDTO.setJoined(et != null);
        }

        return eventDTOS;
    }

    public List<ClassesOption> getClasses(Long eventId) {
        List<ClassesOption> classesOptions = new ArrayList<>();
        classesOptions.add(new ClassesOption(GENERAL, "0", true));

        Event event = eventRepository.getById(eventId);

        classesOptions.addAll(
                eventRepository.findDistinctClasses(eventId).stream()
                        .sorted().collect(Collectors.toList()).stream()
                        .map(x -> new ClassesOption(x, x, false)).collect(Collectors.toList()));

        if (Boolean.TRUE.equals(event.getAwdClassification()))
            classesOptions.add(new ClassesOption(DriveType.AWD.getName(), DriveType.AWD.getName(), false));
        if (Boolean.TRUE.equals(event.getRwdClassification()))
            classesOptions.add(new ClassesOption(DriveType.RWD.getName(), DriveType.RWD.getName(), false));
        if (Boolean.TRUE.equals(event.getFwdClassification()))
            classesOptions.add(new ClassesOption(DriveType.FWD.getName(), DriveType.FWD.getName(), false));

        return classesOptions;
    }

    public void addTeamToEvent(Team team, Long eventId) {
        EventTeam et = eventTeamRepository.findByEventIdAndTeamId(eventId, team.getTeamId());
        Event event = eventRepository.getById(eventId);

        if (et == null)
            et = new EventTeam();

        et.setJoinDate(Instant.now());
        et.setTeamId(team.getTeamId());
        et.setEventId(eventId);
        int number = eventTeamRepository.getMaxNumberByEventId(eventId);
        et.setNumber(number + 1);

        int order = eventTeamRepository.getMaxStartOrderByEventId(eventId);
        et.setOrder(order + 1);

        eventTeamRepository.save(et);

        carService.calculateClass(team, et, event);
        eventTeamRepository.save(et);

        for (Stage stage : event.getStages())
            createEmptyEventScoreIfNeccesarry(stage, et);
    }

    @Transactional
    public void removeTeam(Long eventId, Long teamId) {
        try {
            stageScoreRepository.removeStageScoresByTeamIdAndEventId(eventId, teamId);
            eventTeamRepository.deleteByEventIdAndTeamId(eventId, teamId);

            Event event = eventRepository.getById(eventId);
            event.getStages().stream().forEach(x -> {
                stageScoreRepository.deleteByStageIdAndTeamId(x.getStageId(), teamId);
                penaltyRepository.deleteByStageIdAndTeamId(x.getStageId(), teamId);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void confirmEntryFee(Long eventId, Long teamId) {
        EventTeam eventTeam = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);
        boolean entryFeePaid = eventTeam.getEntryFeePaid() == null ? false : eventTeam.getEntryFeePaid();
        eventTeam.setEntryFeePaid(!entryFeePaid);
        eventTeamRepository.save(eventTeam);
    }

    @Transactional
    public Long createNew(Event event) {
        if (event.getEventId() != null) eventPathsRepository.deleteByEventId(event.getEventId());

        eventRepository.save(event);

        List<EventTeam> eventTeams = eventTeamRepository.findByEventId(event.getEventId());

        for (Stage stage : event.getStages()) {
            eventTeams.stream().forEach(x -> createEmptyEventScoreIfNeccesarry(stage, x));
        }
        return event.getEventId();
    }

    private void createEmptyEventScoreIfNeccesarry(Stage stage, EventTeam et) {
        List<StageScore> stageScores = stageScoreRepository.findByStageIdAndTeamId(stage.getStageId(), et.getTeamId());
        if (stageScores.size() > 1)
            stageScores.stream().filter(x -> x.getScore() == null).forEach(x -> stageScoreRepository.deleteById(x.getId()));

        stageScores = stageScoreRepository.findByStageIdAndTeamId(stage.getStageId(), et.getTeamId());
        if (stageScores.isEmpty())
            createEmptyEventScore(stage, et);
    }

    public boolean checkReferee(Long eventId, Authentication auth) {
        if (auth == null)
            return false;

        Optional<Event> optionalEvent = eventRepository.checkIfUserIsReferee(eventId, auth.getName());
        return optionalEvent.isPresent();
    }

    public List<RefereeOption> getRefereeOptions() {
        List<User> users = userRepository.findAll();
        return users.stream().map(x -> new RefereeOption(x)).collect(Collectors.toList());
    }

    public EventDTO getEvent(Long eventId) {
        Event event = eventRepository.getById(eventId);
        EventDTO eventDTO = new EventDTO(event);
        eventDTO.setStages(event.getStages().stream().map(x -> new StageDTO(x)).collect(Collectors.toList()));
        eventDTO.setReferee(event.getReferee());

        return eventDTO;
    }

    public Boolean deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
        return true;
    }

    public boolean saveFile(MultipartFile file, Long eventId, Long teamId) throws Exception {
        EventTeam eventTeam = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);
        eventTeam.setEntryFeeFile(file.getBytes());
        eventTeamRepository.save(eventTeam);

        return true;
    }

    public ResponseEntity<byte[]> getFile(Long eventId, Long teamId) {
        EventTeam eventTeam = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);

        if (eventTeam == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=" + "potwierdzenie_wplaty_" + teamId);
        if (eventTeam.getEntryFeeFile() != null) headers.setContentLength(eventTeam.getEntryFeeFile().length);

        return new ResponseEntity<>(eventTeam.getEntryFeeFile(), headers, HttpStatus.OK);
    }

    public List<EventTeam> sortByClass(List<EventTeam> teams) {
        List<Integer> forcedNumber = teams.stream().filter(x -> Boolean.TRUE.equals(x.getForcedNumber())).map(x -> x.getNumber()).collect(Collectors.toList());
        int number = teams.stream().filter(x -> !Boolean.TRUE.equals(x.getForcedNumber())).mapToInt(x -> x.getNumber()).min().orElse(teams.size());

        sortIndex++;
        if (sortIndex > 100)
            sortIndex = 1;

        if (sortIndex % 2 == 0)
            teams.sort(Comparator.comparing(EventTeam::getCarClassId).reversed());
        else
            teams.sort(Comparator.comparing(x -> x.getCarClassId()));

        List<EventTeam> reversedTeams = new ArrayList<>();
        int order = number;
        for (EventTeam team : teams) {
            while (forcedNumber.contains(number))
                number++;

            team.setOrder(order++);
            if (!Boolean.TRUE.equals(team.getForcedNumber()))
                team.setNumber(number++);

            reversedTeams.add(team);
        }

        return reversedTeams;
    }

    public boolean saveNumbersAndClasses(List<EventTeam> teams, Long eventId) {
        teams.stream().forEach(x -> eventTeamRepository.save(x));

        List<StageScore> stageScores = stageScoreRepository.findAllByEventId(eventId);

        for (EventTeam eventTeam : teams) {
            stageScores.stream()
                    .filter(x -> x.getTeamId().equals(eventTeam.getTeamId()))
                    .forEach(x -> {
                        x.setTeamNumber(Integer.valueOf(eventTeam.getNumber()));
                        stageScoreRepository.save(x);
                    });
        }

        return true;
    }

    public List<ClassesOption> getEventClassesOptions() {
        List<CarClass> classes = carClassRepository.findAll();
        return classes.stream()
                .map(x -> new ClassesOption(x.getName(), String.valueOf(x.getCarClassId()), false))
                .collect(Collectors.toList());
    }

    public Boolean teamChecked(Long eventId, Long teamId, boolean checked) {
        EventTeam eventTeam = eventTeamRepository.findByEventIdAndTeamId(eventId, teamId);

        if (eventTeam == null) return false;

        eventTeam.setTeamChecked(checked);
        eventTeamRepository.save(eventTeam);

        return true;
    }

    public boolean removeFile(Long fileId, Long eventId) {
        EventFile eventFile = eventFileRepository.findByIdAndEventId(fileId, eventId);
        if (eventFile == null) return false;

        eventFileRepository.delete(eventFile);
        return true;
    }

    public void addFileToEvent(MultipartFile file, Long eventId, String fileName, String desc) {
        EventFile eventFile = new EventFile(file, eventId, fileName, desc);

        eventFileRepository.save(eventFile);
    }

    public ResponseEntity<byte[]> getEventFile(Long fileId) {
        EventFile eventFile = eventFileRepository.getById(fileId);

        if (eventFile == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "attachment; filename=" + eventFile.getFileName());
        headers.setContentLength(eventFile.getFile().length);

        return new ResponseEntity<>(eventFile.getFile(), headers, HttpStatus.OK);
    }

    public void addLogoFile(MultipartFile file, Long eventId) {
        Event event = eventRepository.getById(eventId);

        try {
            event.setLogoPathFile(file.getBytes());
            eventRepository.save(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileDto getLogoPath(Long eventId) {
        Event event = eventRepository.getById(eventId);

        if (event != null && event.getLogoPathFile() != null)
            return new FileDto(event.getLogoPathFile());

        return null;
    }
}
