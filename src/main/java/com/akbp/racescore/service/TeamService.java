package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.selectors.Options;
import com.akbp.racescore.model.dto.selectors.TeamOption;
import com.akbp.racescore.model.dto.selectors.TeamOptionList;
import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.enums.DriveType;
import com.akbp.racescore.model.enums.EnginePetrol;
import com.akbp.racescore.model.repository.CarRepository;
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

    @Autowired
    public TeamService(EventService eventService,
                       StageScoreRepository stageScoreRepository,
                       CarRepository carRepository,
                       TeamRepository teamRepository,
                       UserRepository userRepository) {
        this.eventService = eventService;
        this.stageScoreRepository = stageScoreRepository;
        this.carRepository = carRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;

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
        if (team.getCoDriver() == null || team.getCoDriver().isEmpty())
            team.setCoDriver("");

        if (team.getSportLicense() == null)
            team.setSportLicense(false);

        eventService.addTeamToEvent(team, eventId);
        return "Załoga została utworzona";

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
}
