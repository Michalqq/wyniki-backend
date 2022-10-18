package com.akbp.racescore.service.fileGenerator;

import com.akbp.racescore.model.dto.StageScoreDTO;
import com.akbp.racescore.model.dto.StageScoreSumDTO;
import com.akbp.racescore.model.entity.*;
import com.akbp.racescore.model.entity.dictionary.CarClass;
import com.akbp.racescore.model.repository.EventRepository;
import com.akbp.racescore.model.repository.PenaltyRepository;
import com.akbp.racescore.model.repository.StageScoreRepository;
import com.akbp.racescore.service.ScoreService;
import com.akbp.racescore.utils.ScoreToString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
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
public class ScoreToExcelExporterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreToExcelExporterService.class);

    private Workbook workbook;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StageScoreRepository stageScoreRepository;
    @Autowired
    private PenaltyRepository penaltyRepository;

    public ResponseEntity<byte[]> getScoresFile(Long eventId) throws IOException {
        Event event = eventRepository.getById(eventId);

        List<StageScore> scores = stageScoreRepository.findByStageIdIn(event.getStages().stream().map(x -> x.getStageId()).collect(Collectors.toList()));
        ByteArrayOutputStream out = getOutputStream(event, scores);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "vnd.ms-excel"));
        headers.set("Content-Disposition", "attachment; filename=" + "wyniki" + event.getEventId() + ".xls");
        headers.setContentLength(out.toByteArray().length);

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    private ByteArrayOutputStream getOutputStream(Event event, List<StageScore> scores) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook = new XSSFWorkbook();
        workbook = getScoresWorkbook(event, scores);
        workbook.write(out);
        workbook.close();

        return out;
    }

    private Workbook getScoresWorkbook(Event event, List<StageScore> scores) {
        List<EventTeam> eventTeams = event.getEventTeams();
        List<Stage> stages = event.getStages();

        createSheet("Klasyfikacja generalna", eventTeams, scores, stages, event);

        Set<CarClass> classes = eventTeams.stream().map(x -> x.getCarClass()).collect(Collectors.toSet());

        for (CarClass cc : classes)
            createSheet("Klasa - " + cc.getName(), eventTeams.stream().filter(x -> x.getCarClassId() == cc.getCarClassId()).collect(Collectors.toList()), scores, stages, event);

        return workbook;
    }

    private void createSheet(String sheetName, List<EventTeam> eventTeams, List<StageScore> scores, List<Stage> stages, Event event) {
        Sheet sheet = workbook.createSheet("Wyniki - " + sheetName);
        setColumnWidth(sheet);

        stages = stages.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger(1);

        addEventNameAndLogo(sheet, event, index);

        createTitle(sheet, sheetName, index);
        createHeader(sheet, stages, index.getAndIncrement());

        Stage lastStage = stages.get(stages.size() - 1);

        List<StageScoreSumDTO> sumScoreDto = stageScoreRepository.findSummedScoreByStageId(lastStage.getEventId(), lastStage.getStageId());
        List<StageScoreDTO> scoresDto = scoreService.calculateTime(sumScoreDto);

        List<EventTeam> tempEventTeams = new ArrayList<>(eventTeams);
        scoresDto.forEach(x -> createFinishedTeamScoreRow(sheet, x, tempEventTeams, scores, index));
        sheet.createRow(index.getAndIncrement());

        if (tempEventTeams.size() > 0) {
            createTitle(sheet, "Nie klasyfikowani", index);
            tempEventTeams.stream().forEach(x ->
                    createScoreRow(sheet, x, scores.stream().filter(y -> y.getTeamId().compareTo(x.getTeamId()) == 0).collect(Collectors.toList()), index.getAndIncrement(), false));
        }
        sheet.createRow(index.getAndIncrement());
        sheet.createRow(index.getAndIncrement());
        Row row = sheet.createRow(index.getAndIncrement());
        row.createCell(9).setCellValue("Wyniki wygenerowane za pomocą aplikacji: www.wyniki.online");
    }

    private void addEventNameAndLogo(Sheet sheet, Event event, AtomicInteger index) {
        Row row = sheet.createRow(index.getAndIncrement());

        row.createCell(4).setCellValue(event.getName());
        row.getCell(4).setCellStyle(getFontBold(15));
        sheet.createRow(index.getAndIncrement());
        sheet.createRow(index.getAndIncrement());

        if (event.getLogoPathFile() != null) {
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor logoAnchor = new XSSFClientAnchor();
            logoAnchor.setCol1(0);
            logoAnchor.setRow1(0);

            Picture pict = drawing.createPicture(logoAnchor, workbook.addPicture(event.getLogoPathFile(), Workbook.PICTURE_TYPE_JPEG));
            pict.resize();
            pict.resize(110.0 / pict.getImageDimension().height);
        }
    }

    private void createTitle(Sheet sheet, String title, AtomicInteger index) {
        Row row = sheet.createRow(index.getAndIncrement());

        row.createCell(4).setCellValue(title);
        row.getCell(4).setCellStyle(getFontBold(11));
        sheet.createRow(index.getAndIncrement());
    }

    private void setColumnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 4 * 256);
        sheet.setColumnWidth(1, 4 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 17 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 16 * 256);
        sheet.setColumnWidth(6, 14 * 256);
        sheet.setColumnWidth(7, 7 * 256);
    }

    private void createFinishedTeamScoreRow(Sheet sheet, StageScoreDTO stageScoreDTO, List<EventTeam> eventTeams, List<StageScore> scores, AtomicInteger index) {
        Optional<EventTeam> optionalEventTeam = eventTeams.stream().filter(x -> x.getNumber() == stageScoreDTO.getNumber()).findFirst();
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
        header.createCell(index.getAndIncrement()).setCellValue("Samochód");
        header.createCell(index.getAndIncrement()).setCellValue("Klasa");

        stages.stream()
                .forEach(x -> {
                    header.createCell(index.get()).setCellValue(x.getName());
                    sheet.setColumnWidth(index.getAndIncrement(), 10 * 256);
                });

        header.createCell(index.get()).setCellValue("Suma kar");
        sheet.setColumnWidth(index.getAndIncrement(), 10 * 256);
        header.createCell(index.get()).setCellValue("");
        sheet.setColumnWidth(index.getAndIncrement(), 1 * 256);

        header.createCell(index.get()).setCellValue("Wynik suma");
        sheet.setColumnWidth(index.getAndIncrement(), 11 * 256);
        header.createCell(index.get()).setCellValue("");
        sheet.setColumnWidth(index.getAndIncrement(), 1 * 256);
        header.createCell(index.get()).setCellValue("Suma w min.");
        sheet.setColumnWidth(index.getAndIncrement(), 12 * 256);


        setStyle(index.get(), header, getHeaderStyle());

        header.createCell(index.getAndIncrement()).setCellValue("");
        header.createCell(index.get()).setCellValue("Wyniki w sekundach z karami");
        sheet.setColumnWidth(index.getAndIncrement(), 30 * 256);
        stages.stream()
                .forEach(x -> {
                    header.createCell(index.get()).setCellValue( x.getName());
                    sheet.setColumnWidth(index.getAndIncrement(), 10 * 256);
                });

        return index;
    }

    private void createScoreRow(Sheet sheet, EventTeam et, List<StageScore> scores, int index, boolean classificated) {
        Row row = sheet.createRow(index);

        scores = scores.stream().sorted(Comparator.comparing(x -> x.getStageId())).collect(Collectors.toList());
        LOGGER.info("Scores: " + scores.stream().map(x -> x.toString()).collect(Collectors.joining("\n")));
        LOGGER.info("EventTeam: " + et.toString());

        if (classificated) row.createCell(0).setCellValue(index - 6);

        AtomicInteger index2 = new AtomicInteger(1);

        row.createCell(index2.getAndIncrement()).setCellValue(et.getNumber());
        row.createCell(index2.getAndIncrement()).setCellValue(et.getDriver());
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCoDriver()).orElse("-"));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCoClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(
                Optional.ofNullable(et.getCar()).map(x -> x.getBrand() + " " + x.getModel()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCarClass().getName()).orElse("-"));

        scores.stream().forEach(x -> setScore(row, index2, x));
        Long penalties = setPenaltiesSum(row, index2, scores);
        LOGGER.info("penalties: " + penalties);

        Long sum = scores.stream().filter(x -> !Boolean.TRUE.equals(x.getDisqualified())).mapToLong(x -> Optional.ofNullable(x.getScore()).orElse(0L)).sum() + penalties * 1000;
        LOGGER.info("sum: " + sum);

        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue(ScoreToString.toString(sum));
        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue(Math.round(sum/10.0) / 100.0);

        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue("");
//        scores.stream().forEach(x -> setSecScore(row, index2, x));

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

        cellStyle.setFont(getBold(11));

        return cellStyle;
    }

    private Font getBold(int fontSize) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) ((short) 20 * fontSize));
        return font;
    }

    private CellStyle getFontBold(int fontSize) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getBold(fontSize));
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
                "NU" : getScore(x));
    }

    private void setSecScore(Row row, AtomicInteger index2, StageScore x) {
        if (Boolean.TRUE.equals(x.getDisqualified()) )
            row.createCell(index2.getAndIncrement()).setCellValue("NU");
        else
            row.createCell(index2.getAndIncrement()).setCellValue(getSecScore(x));
    }

    private Long setPenaltiesSum(Row row, AtomicInteger index2, List<StageScore> scores) {
        if (scores.isEmpty()) return 0L;

        Long penaltiesSum = 0L;
        List<Penalty> penalties = penaltyRepository.findByStageIdInAndTeamId(scores.stream().map(x -> x.getStageId()).collect(Collectors.toList()), scores.get(0).getTeamId());
        if (!penalties.isEmpty()) {
            penaltiesSum = penalties.stream().mapToLong(y -> Optional.ofNullable(y.getPenaltySec()).orElse(0L)).sum();
            row.createCell(index2.get()).setCellValue(penaltiesSum + " sek");
        }
        index2.getAndIncrement();

        return penaltiesSum;
    }

    private String getScore(StageScore stageScore) {
        String score = stageScore.getScore() == null ? "" : ScoreToString.toString(stageScore.getScore());
        if (stageScore.getPenalty() != null && stageScore.getPenalty().equals(0L))
            return score + " (T)";

        return score;
    }


    private Double getSecScore(StageScore stageScore) {
        if (stageScore.getScore()==null) return null;

        Double score = stageScore.getScore()/1000.0;

        List<Penalty> penalties = penaltyRepository.findByStageIdAndTeamId(stageScore.getStageId(), stageScore.getTeamId());
        if (!penalties.isEmpty())
            score = score + penalties.stream().mapToLong(y -> Optional.ofNullable(y.getPenaltySec()).orElse(0L)).sum();

        return Math.round(score*100.0)/100.0;
    }
}
