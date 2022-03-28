package com.akbp.racescore.service;

import com.akbp.racescore.email.EmailSenderImpl;
import com.akbp.racescore.model.dto.MsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsgService {

    @Autowired
    private EmailSenderImpl emailSender;

    public String sendMsg(MsgDto msg) {
        boolean result = emailSender.sendMsg(msg);

        return result ? "Wiadomość została wysłana - odpowiemy wkrótce." : "Wysyłanie wiadomości nie powiodło się! Spróbuj ponownie";
    }
}
