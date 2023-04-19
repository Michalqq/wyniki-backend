package com.akbp.racescore.service.fileGenerator;

import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.repository.EventRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ListToExcelExporterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreToExcelExporterService.class);

    private Workbook workbook;
    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity<byte[]> getEventTeamData(Long eventId) throws IOException {
        Event event = eventRepository.getById(eventId);

        ByteArrayOutputStream out = getOutputStream(event);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "vnd.ms-excel"));
        headers.set("Content-Disposition", "attachment; filename=" + "lista_zawodnikow" + event.getName() + ".xls");
        headers.setContentLength(out.toByteArray().length);

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    private ByteArrayOutputStream getOutputStream(Event event) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook = new XSSFWorkbook();
        workbook = getScoresWorkbook(event);
        workbook.write(out);
        workbook.close();

        return out;
    }

    private Workbook getScoresWorkbook(Event event) {
        createSheet("Lista zawodników", event);
        return workbook;
    }

    private void createSheet(String sheetName, Event event) {
        Sheet sheet = workbook.createSheet(sheetName);
        setColumnWidth(sheet);

        AtomicInteger index = new AtomicInteger(1);
        addEventNameAndLogo(sheet, event, index);

        createTitle(sheet, sheetName, index);
        createHeader(sheet, index.getAndIncrement());
        List<EventTeam> teams = event.getEventTeams();
        teams.sort(Comparator.comparing(x -> x.getNumber()));
        teams.forEach(x -> createDataRow(sheet, x, index.getAndIncrement()));

        sheet.createRow(index.getAndIncrement());
        sheet.createRow(index.getAndIncrement());
        Row row = sheet.createRow(index.getAndIncrement());
        row.createCell(9).setCellValue("Wygenerowane za pomocą aplikacji: www.wyniki.online");
    }

    private void addEventNameAndLogo(Sheet sheet, Event event, AtomicInteger index) {
        Row row = sheet.createRow(index.getAndIncrement());

        row.createCell(4).setCellValue(event.getName());
        row.getCell(4).setCellStyle(getFontBold(15));
        sheet.createRow(index.getAndIncrement());
        sheet.createRow(index.getAndIncrement());

    }

    private void createTitle(Sheet sheet, String title, AtomicInteger index) {
        Row row = sheet.createRow(index.getAndIncrement());

        row.createCell(4).setCellValue(title);
        row.getCell(4).setCellStyle(getFontBold(11));
        sheet.createRow(index.getAndIncrement());
    }

    private AtomicInteger createHeader(Sheet sheet, int headerIndex) {
        Row header = sheet.createRow(headerIndex);

        AtomicInteger index = new AtomicInteger(0);

        header.createCell(index.getAndIncrement()).setCellValue("L.p.");
        header.createCell(index.getAndIncrement()).setCellValue("Numer startowy");
        header.createCell(index.getAndIncrement()).setCellValue("Kierowca");
        header.createCell(index.getAndIncrement()).setCellValue("Automobilklub");
        header.createCell(index.getAndIncrement()).setCellValue("Numer telefonu");
        header.createCell(index.getAndIncrement()).setCellValue("Pilot");
        header.createCell(index.getAndIncrement()).setCellValue("Pilot automobilklub");
        header.createCell(index.getAndIncrement()).setCellValue("Numer telefonu - pilot");
        header.createCell(index.getAndIncrement()).setCellValue("Samochód");
        header.createCell(index.getAndIncrement()).setCellValue("Klasa");
        header.createCell(index.getAndIncrement()).setCellValue("Email");
        header.createCell(index.getAndIncrement()).setCellValue("");
        header.createCell(index.getAndIncrement()).setCellValue("Osoba kontaktowa - wypadek");
        header.createCell(index.getAndIncrement()).setCellValue("Numer telefonu - wypadek");

        setStyle(index.get(), header, getHeaderStyle());

        return index;
    }

    private void createDataRow(Sheet sheet, EventTeam et, int index) {
        Row row = sheet.createRow(index);

        LOGGER.info("EventTeam: " + et.toString());

        AtomicInteger index2 = new AtomicInteger(0);

        row.createCell(index2.getAndIncrement()).setCellValue(index - 6);
        row.createCell(index2.getAndIncrement()).setCellValue(et.getNumber());
        row.createCell(index2.getAndIncrement()).setCellValue(et.getDriver());
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getPhone()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCoDriver()).orElse("-"));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCoClub()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getCoPhone()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(
                Optional.ofNullable(et.getCar()).map(x -> x.getBrand() + " " + x.getModel()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getCarClass().getName()).orElse("-"));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getEmail()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue("");
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getEmergencyPerson()).orElse(""));
        row.createCell(index2.getAndIncrement()).setCellValue(Optional.ofNullable(et.getTeam().getEmergencyPhone()).orElse(""));
    }

    private void setColumnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 4 * 256);
        sheet.setColumnWidth(1, 14 * 256);
        sheet.setColumnWidth(2, 22 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 22 * 256);
        sheet.setColumnWidth(5, 22 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 22 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 35 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 25 * 256);
        sheet.setColumnWidth(13, 25 * 256);
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

}
