package com.akbp.racescore.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EventFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "FILE")
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] file;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "DESCRIPTION")
    private String description;

    public EventFile(MultipartFile file, Long eventId, String fileName, String desc) {
        this.eventId = eventId;
        this.fileName = fileName;
        this.description = desc;

        try {
            this.file = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
