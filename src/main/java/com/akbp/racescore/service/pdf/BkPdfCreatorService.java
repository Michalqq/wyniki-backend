package com.akbp.racescore.service.pdf;

import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.EventRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BkPdfCreatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BkPdfCreatorService.class);

    private final float NAME_ROW_Y = 730;
    private final float NAME_ROW_2_Y = 725;

    private final float CAR_1_Y = 690;
    private final float CAR_2_Y = 670;
    private final float CAR_3_Y = 650;

    private final float CAR_1_X = 180;
    private final float CAR_2_X = 445;

    private final float NUMBER_X = 30;
    private final float CLASS_X = 80;
    private final float DRIVER_X = 125;
    private final float CO_DRIVER_X = 265;
    private final float EVENT_X = 400;

    private final String BK_FORM_PATH = "src/main/resources/bk_template/BK.pdf";
    private final String BK_FOR_EVENT_FORM_PATH = "src/main/resources/bk_template/BK_FOR_EVENT.pdf";
    private final String BK_FOR_EVENT_FILLED_PATH = "src/main/resources/bk_template/BK_FOR_EVENT_FILLED.pdf";

    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity<byte[]> createBkForEvent(Long eventId) {
        Event event = eventRepository.getById(eventId);
        LOGGER.info("createBkForEvent " + eventId);

        List<EventTeam> teams = event.getEventTeams();
        byte[] output = null;

        try {
            createPagesForTeams(teams);
            output = getPdf(event, teams);
        } catch (IOException e) {
            LOGGER.error("IOException" + e.getMessage());
            e.printStackTrace();
        } catch (DocumentException e) {
            LOGGER.error("DocumentException" + e.getMessage());
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=" + "dokumenty_oa_bk" + event.getEventId() + ".pdf");
        headers.setContentLength(output.length);

        return new ResponseEntity<>(output, headers, HttpStatus.OK);
    }

    private void createPagesForTeams(List<EventTeam> eventTeams) throws IOException, DocumentException {
        Document doc = new Document();
        PdfReader reader = new PdfReader(BK_FORM_PATH);
        PdfCopy copy = new PdfSmartCopy(doc, new FileOutputStream(BK_FOR_EVENT_FORM_PATH));
        doc.open();
        for (int i = 1; i <= eventTeams.size(); i++)
            copy.addPage(copy.getImportedPage(reader, 1));
        doc.close();
    }

    private byte[] getPdf(Event event, List<EventTeam> eventTeams) {
        try {
            PdfReader reader = new PdfReader(BK_FOR_EVENT_FORM_PATH);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            BaseFont bf = BaseFont.createFont(
                    BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            for (int i = 1; i <= eventTeams.size(); i++) {
                PdfContentByte over = stamper.getOverContent(i);
                fillData(over, event, eventTeams.get(i - 1), bf);
            }
            stamper.close();
            return baos.toByteArray();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillData(PdfContentByte over, Event event, EventTeam et, BaseFont bf) {
        over.beginText();
        over.setFontAndSize(bf, 12);
        over.setTextMatrix(NUMBER_X, NAME_ROW_Y);
        over.showText(et.getNumber().toString());

        over.setTextMatrix(CLASS_X, NAME_ROW_Y);
        over.showText(et.getCarClass().getName());

        Team team = et.getTeam();

        over.setTextMatrix(DRIVER_X, NAME_ROW_Y);
        over.showText(team.getDriver());

        over.setTextMatrix(CO_DRIVER_X, NAME_ROW_Y);
        over.showText(team.getCoDriver());

        fillEventInfo(over, event);

        over.setTextMatrix(CAR_1_X, CAR_1_Y);
        over.showText(team.getCurrentCar().getBrand());
        over.setTextMatrix(CAR_1_X, CAR_2_Y);
        over.showText(team.getCurrentCar().getModel());
        over.setTextMatrix(CAR_1_X, CAR_3_Y);
        String engine = team.getCurrentCar().getEngineCapacity().toString();
        over.showText(engine.substring(0, engine.indexOf('.')));

        over.setTextMatrix(CAR_2_X, CAR_1_Y);
        over.showText(team.getCurrentCar().getLicensePlate());
        over.setTextMatrix(CAR_2_X, CAR_2_Y);
        over.showText(team.getCurrentCar().getVin());

        fillTurboInfo(over, team.getCurrentCar().getTurbo());

        over.endText();
    }

    private void fillEventInfo(PdfContentByte over, Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withZone(ZoneId.systemDefault());

        over.setTextMatrix(EVENT_X, NAME_ROW_Y + 5);
        over.showText(event.getName());

        over.setTextMatrix(EVENT_X + 50, NAME_ROW_2_Y);
        over.showText(formatter.format(event.getDate()));
    }

    private void fillTurboInfo(PdfContentByte over, Boolean turbo) {
        if (Boolean.TRUE.equals(turbo)) {
            over.setTextMatrix(CAR_2_X + 43, CAR_3_Y + 1);
            over.showText("X");
        } else {
            over.setTextMatrix(CAR_2_X + 113, CAR_3_Y + 1);
            over.showText("X");
        }
    }
}
