package com.akbp.racescore.service;

import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.dictionary.CarClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void calculateClass(Team team) {

        try {
            Car car = team.getCurrentCar();
            Double engine = car.getEngineCapacity();

            if (team.getSportLicense()) {
                team.setCarClassId(carClassRepository.findByName(QUEST).getCarClassId());
                return;
            }

            if (engine < 10)
                engine = engine * 1000;
            if (car.getTurbo())
                engine = engine * 1.7;

            if (engine < 1251)
                team.setCarClassId(carClassRepository.findByName(K1).getCarClassId());
            else if (engine < 1601)
                team.setCarClassId(carClassRepository.findByName(K2).getCarClassId());
            else if (engine < 2001)
                team.setCarClassId(carClassRepository.findByName(K3).getCarClassId());
            else if (engine >= 2001)
                team.setCarClassId(carClassRepository.findByName(K4).getCarClassId());

        } catch (Exception e) {
            team.setCarClassId(carClassRepository.findByName(OPEN).getCarClassId());
        }
    }

}
