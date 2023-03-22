package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.StatementOutDto;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.Stage;
import com.akbp.racescore.model.entity.Statement;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.StatementRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementService {

    @Autowired
    private StatementRepository statementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<StatementOutDto> getStatements(Long eventId) {
        return statementRepository.findByEventId(eventId).stream()
                .sorted(Comparator.comparingInt(x-> (int) x.getDate().toEpochMilli()))
                .map(x->{
                    StatementOutDto statementOutDto = modelMapper.map(x, StatementOutDto.class);
                    statementOutDto.setFileExist(x.getFile()!=null);
                    return statementOutDto;
                })
                .collect(Collectors.toList());
    }

    public Long addStatement(Authentication auth, Statement statement) {
        User user = userRepository.findByUsername(auth.getName());
        if (user != null)
            statement.setUserMod(user.getUserId());

        statementRepository.save(statement);

        return statement.getStatementId();
    }

    public String addFileToStatement(MultipartFile postFile, Long statementId) throws IOException {
        if (postFile == null) return "";

        Statement statement = statementRepository.getById(statementId);
        if (statement == null) return "Akcja nie powiodła się";

        statement.setFile(postFile.getBytes());
        statementRepository.save(statement);

        return "Dodano komunikat";
    }

    public void addFinalList(Authentication auth, byte[] file, Event event, Stage stage) {
        if (file == null) return;
        Statement statement = new Statement(event, file);

        User user = userRepository.findByUsername(auth.getName());
        if (user != null)
            statement.setUserMod(user.getUserId());

        String finalListName = "Lista startowa: " + stage.getName();
        removeFinalListStatement(finalListName);

        statement.setName(finalListName);
        statement.setFileName(finalListName + ".pdf");
        statementRepository.save(statement);
    }

    private void removeFinalListStatement(String finalListName) {
        List<Statement> statements = statementRepository.findByName(finalListName);
        if (!statements.isEmpty())
            statements.forEach(x -> statementRepository.delete(x));
    }

    public void deleteStatement(Authentication auth, Long statementId) {
        Statement statement = statementRepository.getById(statementId);
        if (statement == null)
            return;

        if (eventRepository.checkIfUserIsReferee(statement.getEventId(), auth.getName()).isPresent())
            statementRepository.delete(statement);
    }

    public Long getStatementsCount(Long eventId) {
        return statementRepository.countByEventId(eventId);
    }

    public ResponseEntity<byte[]> downloadFile(Long statementId) {
        Statement statement = statementRepository.getById(statementId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "attachment; filename=" + statement.getFileName());
        headers.setContentLength(statement.getFile().length);

        return new ResponseEntity<>(statement.getFile(), headers, HttpStatus.OK);
    }
}
