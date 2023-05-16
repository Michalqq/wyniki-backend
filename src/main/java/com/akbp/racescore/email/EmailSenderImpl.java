package com.akbp.racescore.email;

import com.akbp.racescore.model.dto.MsgDto;
import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailSenderImpl implements EmailSender {
    public static final String EMAIL = "kraciukmichal@gmail.com";

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${racescore.app.path}")
    private String appPath;

    @Value("${racescore.email.from1}")
    private String emailFrom1;

    @Value("${racescore.email.from2}")
    private String emailFrom2;

    @Value("${insurance.mubi.link}")
    private String mubiLink;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String PATTERN_FORMAT = "dd-MM-yyyy";

    @Override
    public boolean sendEmail(Long attemptCount, String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(attemptCount == 1L ? emailFrom1 : emailFrom2);
            helper.setReplyTo(EMAIL);
            helper.setSubject(title);
            helper.setText(content, true);

            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile("src/main/resources/static/wynikiLogo.jpg");
            imagePart.setContentID("<image>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.setFileName("logo.jpg");

            helper.getMimeMultipart().addBodyPart(imagePart);
            javaMailSender.send(mail);
            log.info("Sent email " + to + " with msg: " + content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return attemptCount == 1L ? sendEmail(2L, to, title, content) : false;
        }

    }

    public boolean sendPasswordReminderEmail(User user) {
        String token = jwtUtils.generateJwtToken(user.getUsername());
        String content = getHtmlStart();

        content += "Twój login to: " + user.getUsername();
        content += " <br></br> ";
        content += "Aby zresetować hasło kliknij w link umieszczony poniżej <br></br> ";

        content += "<a href=http://" + appPath + "/passwordReset?" + token + ">Link do resetowania hasła (kliknij w tekst)</a>";

        content += "<br></br><br></br><br></br>";
        content += "Jeśli nie prosiłeś o reset hasła zignoruj tę wiadomość.";
        content += getHtmlEnd();

        return sendEmail(1L, user.getEmail(), "Wyniki.online - resetowanie hasła", content);
    }

    public boolean sendInsuranceNotification(Team team, Car car) {
        String content = getHtmlStart();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        String insuranceExpiryDate = formatter.format(car.getInsuranceExpiryDate());

        content += "<h2>Cześć " + team.getDriver() + "</h2>";
        content += "<p><br></br></p>";

        content += "<h4>tutaj Michał ze strony: <a href=https://www.wyniki.online>www.wyniki.online</a> - Wyniki motorsportowe online AKBP</h4>";
        content += "<p><br></br></p>";
        content += "<h3>W Twojej rajdówce: " + car.getBrand() + " " + car.getModel() + " nr. rej. " + car.getLicensePlate() + " ubezpieczenie skończy się dnia: "
                + insuranceExpiryDate + "</h3>";
        content += "Już dziś wykup ubezpieczenie taniej.";

        content += "<p>Stronę z wynikami stworzyłem hobbystycznie w celach promocyjnych amatorskich imprez samochodowych.<br></br>" +
                "Jeśli podobają Ci się wyniki online czy zapisy na imprezy i chciałbyś mnie wesprzeć to możesz skorzystać z linku " +
                "polecającego poniżej który pozwoli Ci kupić ubezpieczenie <b>150zł TANIEJ!!! za pomocą MUBI</b></p>";
        content += "<p>Mubi to porównywarka ubezpieczeń z której sam korzystam.</p><br></br>";

        content += "<h2><a href=" + mubiLink + ">" + mubiLink + "</a></h2>";
        content += "<h3>Ubezpieczenie ze zwrotem 150zł !!! - będzie na wpisowe lub paliwo :)</h3>";

        content += getHtmlEnd();

        return sendEmail(1L, team.getEmail(), "Kończy się ubezpieczenie w Twojej rajdówce: " + car.getBrand() + " " + car.getModel(), content);
    }

    public boolean sendCarInspectionNotification(Team team, Car car) {
        String content = getHtmlStart();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        String insuranceExpiryDate = formatter.format(car.getInsuranceExpiryDate());

        content += "<h2>Cześć " + team.getDriver() + "</h2>";
        content += "<p><br></br></p>";

        content += "<h4>tutaj Michał ze strony: <a href=https://www.wyniki.online>www.wyniki.online</a> - Wyniki motorsportowe online AKBP</h4>";
        content += "<p><br></br></p>";
        content += "<h3>W Twojej rajdówce: " + car.getBrand() + " " + car.getModel() + " nr. rej. " + car.getLicensePlate() + " przegląd techniczny skończy się dnia: "
                + insuranceExpiryDate + "</h3>";
        content += "Nie zapomnij podbić przeglądu przed kolejnymi zawodami!";

        content += getHtmlEnd();

        return sendEmail(1L, team.getEmail(), "Kończy się przegląd w Twojej rajdówce: " + car.getBrand() + " " + car.getModel(), content);
    }

    public boolean sendMsg(MsgDto msg) {
        String content = getHtmlStart();

        content += msg.getMessage();

        content += "<br></br>";
        content += "Email do odpowiedzi: " + msg.getEmail();
        content += "<br></br>";
        content += "Telefon: " + msg.getPhone();

        content += getHtmlEnd();

        return sendEmail(1L, "kraciukmichal@gmail.com", msg.getTitle(), content);
    }

    private String getHtmlStart() {

        return "<!DOCTYPE html> <html lang=\"en\"><head> <img src=\"cid:image\">";
    }

    private String getHtmlEnd() {
        String content = "<br></br>";
        content += "__________________________________________________";
        content += "<br></br>";
        content += "<h6>Ten email został wygenerowany automatycznie.</h6>";
        content += "</head></html>";
        return content;
    }
}
