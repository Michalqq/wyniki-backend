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
import java.util.List;

@Component
public class InsuranceEmailNotify {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmailSenderImpl emailSender;

    @Value("${insurance.notify1.before.day.count}")
    private Long notify1DayCount;
    @Value("${insurance.notify2.before.day.count}")
    private Long notify2DayCount;

    //@Scheduled(cron = "0/2 * * * * ?")
    @Scheduled(cron = "0 55 19 * * ?", zone = "Europe/Warsaw")
    public void sendEmail() {
        send(14L);
//        send(notify1DayCount);
//        send(notify2DayCount);
    }

    private void send(Long dayCount) {
        Instant startCount = Instant.now().plus(dayCount, ChronoUnit.DAYS);
        Instant start = startCount.minus(1, ChronoUnit.DAYS);
        List<Car> cars = carRepository.findByInsuranceExpiryDateBetween(Instant.now(), startCount);

        emailSender.sendEmail(1L, "kraciukmichal@gmail.com", "Serwis sprawdził", "Znaleziono " + cars.size() + "przedział " + startCount + " do " + start);

        cars.stream().forEach(x -> emailSender.sendInsuranceNotification(teamRepository.findByTeamId(x.getTeamId()), x));
    }
}
