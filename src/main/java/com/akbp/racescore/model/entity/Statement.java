package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.dto.StatementInDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STATEMENT_ID", updatable = false, nullable = false)
    private Long statementId;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FILE")
    private byte[] file;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "DATE")
    private Instant date;

    @Column(name = "USER_MOD")
    private Long userMod;

    public Statement(StatementInDto statementInDto) throws IOException {
        this.eventId = statementInDto.getEventId();
        this.name = statementInDto.getName();
        this.description = statementInDto.getDescription();
        this.fileName = statementInDto.getFileName();
        this.date = statementInDto.getDate();

        if (statementInDto.getPostFile() != null)
            this.file = statementInDto.getPostFile().getBytes();
    }
}
