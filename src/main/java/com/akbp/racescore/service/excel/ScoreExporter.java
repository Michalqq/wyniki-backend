package com.akbp.racescore.service.excel;

import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.PenaltyRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.utils.ScoreToString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScoreExporter {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StageScoreRepository stageScoreRepository;
    @Autowired
    private PenaltyRepository penaltyRepository;

    public ResponseEntity<byte[]> getScoresFile(Long eventId) throws IOException {
        Event event = eventRepository.getById(eventId);

        List<StageScore> scores = stageScoreRepository.findByStageIdIn(event.getStages().stream().map(x -> x.getStageId()).collect(Collectors.toList()));

        ByteArrayOutputStream out = getOutputStream(event.getEventTeams(), scores, event.getStages());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "vnd.ms-excel"));
        headers.set("Content-Disposition", "attachment; filename=" + "wyniki" + event.getEventId() + ".xls");
        headers.setContentLength(out.toByteArray().length);

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    private ByteArrayOutputStream getOutputStream(List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Workbook workbook = getScoresWorkbook(eventTeams, scores, stages);
        workbook.write(out);
        workbook.close();

        return out;
    }

    private Workbook getScoresWorkbook(List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Wyniki");

        stages = stages.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());
        createHeader(sheet, stages);

        AtomicInteger index = new AtomicInteger(1);
        eventTeams.stream().forEach(x ->
                createScoreRow(sheet, x, scores.stream().filter(y -> y.getTeamId() == x.getTeamId()).collect(Collectors.toList()), index.getAndIncrement()));

        return workbook;
    }

    private void createHeader(Sheet sheet, List<Stage> stages) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("L.p.");
        header.createCell(1).setCellValue("Kierowca");
        header.createCell(2).setCellValue("Pilot");
        header.createCell(3).setCellValue("Klasa");

        AtomicInteger index = new AtomicInteger(4);
        stages.stream()
                .forEach(x -> {
                    header.createCell(index.getAndIncrement()).setCellValue(x.getName() + " - wynik");
                    header.createCell(index.getAndIncrement()).setCellValue(x.getName() + " - kary");
                });

        header.createCell(index.getAndIncrement()).setCellValue("Suma");
        header.createCell(index.getAndIncrement()).setCellValue("Suma w minutach");
        header.createCell(index.getAndIncrement()).setCellValue("Czy klasyfikowany?");
    }

    private void createScoreRow(Sheet sheet, EventTeam et, List<StageScore> scores, int index) {
        Row row = sheet.createRow(index);

        scores = scores.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());

        row.createCell(0).setCellValue(index - 1);
        row.createCell(1).setCellValue(et.getTeam().getDriver());
        row.createCell(2).setCellValue(Optional.ofNullable(et.getTeam().getCoDriver()).orElse("-"));
        row.createCell(3).setCellValue(Optional.ofNullable(et.getCarClass().getName()).orElse("-"));

        AtomicInteger index2 = new AtomicInteger(4);
        scores.stream().forEach(x -> setScore(row, index2, x));

        Long sum = scores.stream().mapToLong(x -> Optional.ofNullable(x.getScore()).orElse(0L)).sum();

        row.createCell(index2.getAndIncrement()).setCellValue(ScoreToString.toString(sum));
        row.createCell(index2.getAndIncrement()).setCellValue(sum / 1000.0);

        row.createCell(index2.getAndIncrement()).setCellValue(scores.stream().filter(x -> x.getScore() == null || x.getDisqualified()).findAny().isPresent() ? "NIE" : "TAK");
    }

    private void setScore(Row row, AtomicInteger index2, StageScore x) {
        row.createCell(index2.getAndIncrement()).setCellValue(x.getDisqualified() ?
                "NU" : getScore(x.getScore()));

        List<Penalty> penalties = penaltyRepository.findByStageIdAndTeamId(x.getStageId(), x.getTeamId());
        if (!penalties.isEmpty())
            row.createCell(index2.get()).setCellValue(penalties.stream().mapToLong(y -> y.getPenaltySec()).sum() + " sek");
        index2.getAndIncrement();
    }

    private String getScore(Long score) {
        return score == null ? "" : ScoreToString.toString(score);
    }

}
