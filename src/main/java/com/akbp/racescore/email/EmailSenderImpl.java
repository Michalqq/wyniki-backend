package com.akbp.racescore.email;

import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderImpl implements EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${racescore.app.path}")
    private String appPath;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean sendEmail(String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo("testowekonto9090@gmail.com");
            helper.setSubject(title);
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        javaMailSender.send(mail);
        return true;
    }

    public boolean sendPasswordReminderEmail(User user) {
        String token = jwtUtils.generateJwtToken(user.getUsername());
        String content = "<!DOCTYPE html> <html lang=\"en\"><head>";

        content += "Aby zresetować hasło kliknij w link umieszczony poniżej <br></br> ";

        content += "<a href=" + appPath + "/passwordReset?" + token + ">Link do resetowania hasła</a>";

        content += "<br></br><br></br><br></br>";
        content += "Jeśli nie prosiłeś o reset hasła zignoruj tę wiadomość.";
        content += "</head></html>";

        return sendEmail(user.getEmail(), "Wyniki.online - resetowanie hasła", content);
    }
}
