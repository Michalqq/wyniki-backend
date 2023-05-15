package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.StatementOutDto;
import com.akbp.racescore.model.entity.Statement;
import com.akbp.racescore.service.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/statement", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class StatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private StatementService statementService;

    @GetMapping("/getStatements")
    public List<StatementOutDto> getStatements(@RequestParam("eventId") Long eventId) {
        try {
            return statementService.getStatements(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @GetMapping("/getStatementsCount")
    public Long getStatementsCount(@RequestParam("eventId") Long eventId) {
        try {
            return statementService.getStatementsCount(eventId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return 0L;
        }
    }
    @PostMapping("/addStatement")
    public Long addStatement(Authentication auth, @RequestBody Statement statement) {
        try {
            return statementService.addStatement(auth, statement);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1L;
        }
    }

    @PostMapping("/addFileToStatement")
    public String addFileToStatement(Authentication auth,
                                     @RequestBody MultipartFile file,
                                     @RequestParam Long statementId) {
        try {
            return statementService.addFileToStatement(auth, file, statementId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }

    @DeleteMapping("/deleteStatement")
    public void deleteStatement(Authentication auth, @RequestParam Long statementId) {
        try {
            statementService.deleteStatement(auth, statementId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("statementId") Long statementId) {
        try {
            return statementService.downloadFile(statementId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
