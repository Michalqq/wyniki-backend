package com.akbp.racescore.scheduled;

import com.akbp.racescore.email.EmailSenderImpl;
import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.repository.CarRepository;
import com.akbp.racescore.model.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class CarInspectionEmailNotification {


    @Autowired
    private CarRepository carRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmailSenderImpl emailSender;

    @Value("${insurance.notify2.before.day.count}")
    private Long notifyDayCount;

    @Scheduled(cron = "0 00 19 * * ?", zone = "Europe/Warsaw")
    public void sendEmail() {
        send(notifyDayCount);
    }

    private void send(Long dayCount) {
        Instant startCount = Instant.now().plus(dayCount, ChronoUnit.DAYS);
        Instant start = startCount.minus(1, ChronoUnit.DAYS).minusSeconds(60);
        List<Car> cars = carRepository.findByInsuranceExpiryDateBetween(start, startCount);

        cars.stream().forEach(x -> emailSender.sendCarInspectionNotification(teamRepository.findByTeamId(x.getTeamId()), x));
    }

}
