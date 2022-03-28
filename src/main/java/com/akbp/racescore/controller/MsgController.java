package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.MsgDto;
import com.akbp.racescore.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/message", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class MsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgController.class);

    @Autowired
    private MsgService msgService;

    @PostMapping("sendMsg")
    public String sendMsg(@RequestBody MsgDto msg) {
        LOGGER.info("sendMsg");
        try {
            return msgService.sendMsg(msg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return "Wysyłanie wiadomości nie powiodło się! Spróbuj ponownie";
    }
}
