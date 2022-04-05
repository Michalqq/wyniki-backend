package com.akbp.racescore.service;

import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.repository.dictionary.CarClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class CarService {

    @Autowired
    private CarClassRepository carClassRepository;

    private static final String OPEN = "OPEN";
    private static final String QUEST = "GOŚĆ";
    private static final String K1 = "K1";
    private static final String K2 = "K2";
    private static final String K3 = "K3";
    private static final String K4 = "K4";
    private static final String K5 = "K5";

    public void calculateClass(Team team, EventTeam et, Event event) {

        try {
            Car car = team.getCurrentCar();
            Double engine = car.getEngineCapacity();

            if (team.getSportLicense()) {
                et.setCarClassId(carClassRepository.findByName(QUEST).getCarClassId());
                return;
            }

//            //TODO
//            Optional<DriveType> driveTypeOpt = Stream.of(DriveType.values())
//                    .filter(v -> v.getId() == Long.valueOf(team.getCurrentCar().getDriveType())).findFirst();
//            if (driveTypeOpt.isPresent() && driveTypeOpt.get().equals(DriveType.RWD)) {
//                et.setCarClassId(carClassRepository.findByName(K5).getCarClassId());
//                return;
//            }
//            //TODO

            if (engine < 10)
                engine = engine * 1000;
            if (car.getTurbo())
                engine = engine * 1.7;

            Double finalEngine = engine;
            EventClasses eventClass = event.getEventClasses().stream()
                    .filter(x -> !x.getCarClass().getName().equals(QUEST))
                    .filter(x -> x.getMaxEngineCapacity() > finalEngine)
                    .min(Comparator.comparingLong(x -> x.getCarClassId())).get();

            et.setCarClassId(eventClass.getCarClassId());

        } catch (Exception e) {
            et.setCarClassId(carClassRepository.findByName(OPEN).getCarClassId());
        }
    }

}
