package com.akbp.racescore.controller;

import com.akbp.racescore.service.fileGenerator.ListToExcelExporterService;
import com.akbp.racescore.service.fileGenerator.OaDocumentPdfCreatorService;
import com.akbp.racescore.service.fileGenerator.ScoreToExcelExporterService;
import com.akbp.racescore.service.fileGenerator.BkPdfCreatorService;
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
    private OaDocumentPdfCreatorService fileService;
    @Autowired
    private ScoreToExcelExporterService scoreExporter;
    @Autowired
    private BkPdfCreatorService bkPdfCreatorService;
    @Autowired
    private ListToExcelExporterService listToExcelExporterService;

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

    @GetMapping("getEventTeamExcelData")
    public ResponseEntity<byte[]> getEventTeamExcelData(Long eventId) {
        LOGGER.info("getEventTeamExcelData");
        try {
            return listToExcelExporterService.getEventTeamData(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("getBkFiles")
    public ResponseEntity<byte[]> getBkFiles(Long eventId) {
        LOGGER.info("getBkFiles");
        try {
            return bkPdfCreatorService.getBkFiles(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
