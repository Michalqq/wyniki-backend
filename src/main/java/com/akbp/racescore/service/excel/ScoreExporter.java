package com.akbp.racescore.service.excel;

import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.entity.dictionary.CarClass;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.PenaltyRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.utils.ScoreToString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScoreExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreExporter.class);

    private Workbook workbook;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StageScoreRepository stageScoreRepository;
    @Autowired
    private PenaltyRepository penaltyRepository;

    public ResponseEntity<byte[]> getScoresFile(Long eventId) throws IOException {
        Event event = eventRepository.getById(eventId);

        List<StageScore> scores = stageScoreRepository.findByStageIdIn(event.getStages().stream().map(x -> x.getStageId()).collect(Collectors.toList()));
        LOGGER.info(scores.toString());

        ByteArrayOutputStream out = getOutputStream(event.getEventTeams(), scores, event.getStages());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "vnd.ms-excel"));
        headers.set("Content-Disposition", "attachment; filename=" + "wyniki" + event.getEventId() + ".xls");
        headers.setContentLength(out.toByteArray().length);

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    private ByteArrayOutputStream getOutputStream(List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook = new XSSFWorkbook();
        workbook = getScoresWorkbook(eventTeams, scores, stages);
        workbook.write(out);
        workbook.close();

        return out;
    }

    private Workbook getScoresWorkbook(List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages) {
        createSheet("Klasyfikacja generalna", eventTeams, scores, stages);

        Set<CarClass> classes = eventTeams.stream().map(x -> x.getCarClass()).collect(Collectors.toSet());

        for (CarClass cc : classes)
            createSheet("Klasa - " + cc.getName(), eventTeams.stream().filter(x -> x.getCarClassId() == cc.getCarClassId()).collect(Collectors.toList()), scores, stages);

        return workbook;
    }

    private void createSheet(String sheetName, List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages) {
        Sheet sheet = workbook.createSheet("Wyniki - " + sheetName);
        setColumnWidth(sheet);

        stages = stages.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger(1);

        createTitle(sheet, sheetName, index);
        createHeader(sheet, stages, index.getAndIncrement());

        Stage lastStage = stages.get(stages.size() - 1);

        List<StageScoreSumDTO> sumScoreDto = stageScoreRepository.findSummedScoreByStageId(lastStage.getEventId(), lastStage.getStageId());
        List<EventTeam> tempEventTeams = new ArrayList<>(eventTeams);
        sumScoreDto.forEach(x -> createFinishedTeamScoreRow(sheet, x, tempEventTeams, scores, index));
        sheet.createRow(index.getAndIncrement());

        if (tempEventTeams.size() > 0) {
            createTitle(sheet, "Nie klasyfikowani", index);
            tempEventTeams.stream().forEach(x ->
                    createScoreRow(sheet, x, scores.stream().filter(y -> y.getTeamNumber() == x.getNumber()).collect(Collectors.toList()), index.getAndIncrement(), false));
        }
    }

    private void createTitle(Sheet sheet, String title, AtomicInteger index) {
        Row row = sheet.createRow(index.getAndIncrement());

        row.createCell(3).setCellValue(title);
        row.getCell(3).setCellStyle(getFontBold());
        sheet.createRow(index.getAndIncrement());
    }

    private void setColumnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 4 * 256);
        sheet.setColumnWidth(1, 4 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 8 * 256);
    }

    private void createFinishedTeamScoreRow(Sheet sheet, StageScoreSumDTO stageScoreSumDTO, List<EventTeam> eventTeams, List<StageScore> scores, AtomicInteger index) {
        Optional<EventTeam> optionalEventTeam = eventTeams.stream().filter(x -> x.getNumber() == stageScoreSumDTO.getNumber()).findFirst();
        if (optionalEventTeam.isEmpty()) return;

        EventTeam et = optionalEventTeam.get();
        createScoreRow(sheet, et, scores.stream().filter(y -> y.getTeamId().compareTo(et.getTeamId()) == 0).collect(Collectors.toList()), index.getAndIncrement(), true);
        eventTeams.remove(et);
    }

    private AtomicInteger createHeader(Sheet sheet, List<Stage> stages, int headerIndex) {
        Row header = sheet.createRow(headerIndex);

        AtomicInteger index = new AtomicInteger(0);

        header.createCell(index.getAndIncrement()).setCellValue("Poz");
        header.createCell(index.getAndIncrement()).setCellValue("#Nr");
        header.createCell(index.getAndIncrement()).setCellValue("Kierowca");
        header.createCell(index.getAndIncrement()).setCellValue("Automobilklub");
        header.createCell(index.getAndIncrement()).setCellValue("Pilot");
        header.createCell(index.getAndIncrement()).setCellValue("Automobilklub");
        header.createCell(index.getAndIncrement()).setCellValue("Klasa");

        stages.stream()
                .forEach(x -> {
                    header.createCell(index.get()).setCellValue(x.getName());
                    sheet.setColumnWidth(index.getAndIncrement(), 10 * 256);
                });

        header.createCell(index.get()).setCellValue("Suma kar");
        sheet.setColumnWidth(index.getAndIncrement(), 13 * 256);
        header.createCell(index.get()).setCellValue("");
        sheet.setColumnWidth(index.getAndIncrement(), 3 * 256);

        header.createCell(index.get()).setCellValue("Wynik suma");
        sheet.setColumnWidth(index.getAndIncrement(), 13 * 256);
        header.createCell(index.get()).setCellValue("");
        sheet.setColumnWidth(index.getAndIncrement(), 3 * 256);
        header.createCell(index.get()).setCellValue("Suma w minutach");
        sheet.setColumnWidth(index.getAndIncrement(), 15 * 256);

        setStyle(index.get(), header, getHeaderStyle());

        return index;
    }

    private void createScoreRow(Sheet sheet, EventTeam et, List<StageScore> scores, int index, boolean classificated) {
        Row row = sheet.createRow(index);

        scores = scores.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());
        LOGGER.info(scores.toString());

        if (classificated) row.createCell(0).setCellValue(index - 3);

        AtomicInteger index2 = new AtomicInteger(1);

        row.createCell(index2.getAndIncrement()).setCellValue(et.getNumber());
        row.createCell(index2.getAndIncrement()).setCellValue(et.getTeam().getDriver());
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getCoDriver()).orElse("-"));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getCoClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCarClass().getName()).orElse("-"));

        scores.stream().forEach(x -> setScore(row, index2, x));
        setPenaltiesSum(row, index2, scores);

        Long sum = scores.stream().filter(x -> !Boolean.TRUE.equals(x.getDisqualified())).mapToLong(x -> Optional.ofNullable(x.getScore()).orElse(0L)).sum();

        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue(ScoreToString.toString(sum));
        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue(sum / 1000.0);

        if (index % 2 == 0)
            setStyle(index2.get(), row, getDarkerStyle());

        //row.createCell(index2.getAndIncrement()).setCellValue(scores.stream().filter(x -> x.getScore() == null || Boolean.TRUE.equals(x.getDisqualified())).findAny().isPresent() ? "NIE" : "TAK");
    }

    private void setStyle(int index, Row row, CellStyle cellStyle) {
        for (int i = 0; i < index; i++) {
            Cell tempCell = row.getCell(i);
            if (tempCell != null)
                tempCell.setCellStyle(cellStyle);
            else
                row.createCell(i).setCellStyle(getDarkerStyle());
        }
    }

    private CellStyle getHeaderStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cellStyle.setFont(getBold());

        return cellStyle;
    }

    private Font getBold() {
        Font font = workbook.createFont();
        font.setBold(true);
        return font;
    }

    private CellStyle getFontBold() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getBold());
        return cellStyle;
    }

    private CellStyle getDarkerStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    private void setScore(Row row, AtomicInteger index2, StageScore x) {
        row.createCell(index2.getAndIncrement()).setCellValue(Boolean.TRUE.equals(x.getDisqualified()) ?
                "NU" : getScore(x.getScore()));
    }

    private void setPenaltiesSum(Row row, AtomicInteger index2, List<StageScore> scores) {
        if (scores.isEmpty()) return;

        List<Penalty> penalties = penaltyRepository.findByStageIdInAndTeamId(scores.stream().map(x -> x.getStageId()).collect(Collectors.toList()), scores.get(0).getTeamId());
        if (!penalties.isEmpty())
            row.createCell(index2.get()).setCellValue(penalties.stream().mapToLong(y -> y.getPenaltySec()).sum() + " sek");
        index2.getAndIncrement();
    }

    private String getScore(Long score) {
        return score == null ? "" : ScoreToString.toString(score);
    }
}
