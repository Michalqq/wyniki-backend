package com.akbp.racescore.controller;

import com.akbp.racescore.service.FileService;
import com.akbp.racescore.service.excel.ScoreExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/file", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;
    @Autowired
    private ScoreExporter scoreExporter;

    @GetMapping("getEventTeamsData")
    public ResponseEntity<byte[]> getEventTeamsData(Long eventId) {
        LOGGER.info("getEventTeamsData");
        try {
            return fileService.getEventTeamsData(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("getScoresFile")
    public ResponseEntity<byte[]> getScoresFile(Long eventId) {
        LOGGER.info("getScoresFile");
        try {
            return scoreExporter.getScoresFile(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
