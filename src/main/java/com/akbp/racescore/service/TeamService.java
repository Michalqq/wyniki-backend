package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.selectors.Options;
import com.akbp.racescore.model.dto.selectors.TeamOption;
import com.akbp.racescore.model.dto.selectors.TeamOptionList;
import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.enums.DriveType;
import com.akbp.racescore.model.enums.EnginePetrol;
import com.akbp.racescore.model.repository.CarRepository;
import com.akbp.racescore.model.repository.EventTeamRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.model.repository.TeamRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final EventService eventService;
    private final StageScoreRepository stageScoreRepository;
    private final CarRepository carRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final EventTeamRepository eventTeamRepository;

    @Autowired
    public TeamService(EventService eventService,
                       StageScoreRepository stageScoreRepository,
                       CarRepository carRepository,
                       TeamRepository teamRepository,
                       UserRepository userRepository,
                       EventTeamRepository eventTeamRepository) {
        this.eventService = eventService;
        this.stageScoreRepository = stageScoreRepository;
        this.carRepository = carRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.eventTeamRepository = eventTeamRepository;

    }

    public List<TeamOption> getTeamOptions(Long eventId, Long stageId, String mode) {
        List<EventTeam> eventTeams = eventTeamRepository.findByEventId(eventId);
        List<StageScore> scores = new ArrayList<>();
        if (mode.equals("NEW"))
            scores = stageScoreRepository.findByStageIdAndScoreIsNullAndDisqualifiedFalse(stageId);
        else if (mode.equals("EDIT"))
            scores = stageScoreRepository.findByStageIdAndScoreIsNotNullAndDisqualifiedFalse(stageId);
        else if (mode.equals("PENALTY"))
            scores = stageScoreRepository.findByStageIdAndDisqualifiedFalse(stageId);

        return scores.stream()
                .sorted(Comparator.comparingLong(StageScore::getTeamNumber))
                .map(x -> new TeamOption(x.getTeamNumber() + " - " + eventTeams.stream().filter(et -> et.getTeamId() == x.getTeamId()).findFirst().get().getDriver(), x.getTeam().getTeamId().toString(), false))
                .collect(Collectors.toList());
    }

    public Team saveTeam(Team team) {
        if (team.getCoDriver() == null || team.getCoDriver().isEmpty())
            team.setCoDriver("");

        if (team.getSportLicense() == null)
            team.setSportLicense(false);

        if (team.getCurrentCar() != null && team.getCurrentCar().getTeamId() == null) {
            Car tempCar = team.getCurrentCar();
            team.setCurrentCar(null);
            team = teamRepository.save(team);
            tempCar.setTeamId(team.getTeamId());
            team.setCurrentCar(tempCar);
        }
        team = teamRepository.save(team);

        return team;
    }

    public Long addTeam(Team team, Long eventId) {
        team = saveTeam(team);

        eventService.addTeamToEvent(team, eventId);
        return team.getTeamId();
    }

    public Car getCar(Long carId) {
        return carRepository.getById(carId);
    }

    public String addCar(Long teamId, Car car) {
        Team team = teamRepository.getById(teamId);
        team.setCurrentCar(car);
        teamRepository.save(team);
        return "Dodano samochód";
    }

    public TeamOptionList getTeamOptionList() {
        List<Options> driveTypeOption = Arrays.stream(DriveType.values())
                .map(x -> new TeamOption(x.getName(), String.valueOf(x.getId()), false))
                .collect(Collectors.toList());
        List<Options> petrolOption = Arrays.stream(EnginePetrol.values())
                .map(x -> new TeamOption(x.name(), x.name(), false))
                .collect(Collectors.toList());

        return new TeamOptionList(driveTypeOption, petrolOption);
    }

    public Team getTeam(Authentication auth) {
        if (auth == null)
            return null;

        User user = userRepository.findByUsername(auth.getName());
        if (user == null)
            return null;

        Team team = teamRepository.findByUserId(user.getUserId());
        if (team == null)
            return createEmptyTeam(user);

        return team;
    }

    private Team createEmptyTeam(User user) {
        Team team = new Team();
        team.setUserId(user.getUserId());
        team.setEmail(user.getEmail());
        team.setDriver("");
        teamRepository.save(team);

        return team;
    }

    public Team getTeamByTeamId(Long teamId) {
        return teamRepository.findByTeamId(teamId);
    }
}
