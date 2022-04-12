package com.akbp.racescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping({"/", "/event", "/login", "/register", "/reminder", "/passwordReset", "/message", "/teamPanel"})
    public String appPage(Model model) {
        model.addAttribute("version", "v1");
        model.addAttribute("time", "sd");

        return "app";
    }
}
