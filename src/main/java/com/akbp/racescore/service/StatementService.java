package com.akbp.racescore.service;

import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.Stage;
import com.akbp.racescore.model.entity.Statement;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.StatementRepository;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StatementService {

    @Autowired
    private StatementRepository statementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    public List<Statement> getStatements(Long eventId) {
        return statementRepository.findByEventId(eventId);
    }

    public Long addStatement(Authentication auth, Statement statement) throws IOException {
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
}
