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

    public void calculateClass(Team team, EventTeam et, Event event) {
        if (Boolean.TRUE.equals(event.getCarClassManual())) return;

        try {
            Car car = et.getCar();
            Double engine = car.getEngineCapacity();

            if (team.getSportLicense()) {
                et.setCarClassId(carClassRepository.findByName(QUEST).getCarClassId());
                return;
            }

//            //TODO
//            Optional<DriveType> driveTypeOpt = Stream.of(DriveType.values())
//                    .filter(v -> v.getId() == Long.valueOf(et.getCar().getDriveType())).findFirst();
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
