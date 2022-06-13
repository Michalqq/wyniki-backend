package com.akbp.racescore.service.fileGenerator;

import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.repository.EventRepository;
import com.itextpdf.text.*;
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

    private final float DRIVING_LICENSE_Y = 588;

    private final float CAR_1_Y = 690;
    private final float CAR_2_Y = 670;
    private final float CAR_3_Y = 650;

    private final float CAR_1_X = 180;
    private final float CAR_2_X = 445;

    private final float NUMBER_X = 30;
    private final float CLASS_X = 80;
    private final float DRIVER_X = 125;
    private final float CO_DRIVER_X = 262;
    private final float EVENT_X = 395;

    private final float DRIVING_LICENSE_X = 270;

    private final String BK_FORM_PATH = "src/main/resources/bk_template/BK.pdf";
    private final String BK_FOR_EVENT_FORM_PATH = "src/main/resources/bk_template/BK_FOR_EVENT.pdf";

    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity<byte[]> getBkFiles(Long eventId) {
        Event event = eventRepository.getById(eventId);
        LOGGER.info("createBkForEvent " + eventId);

        byte[] file = createBkForEvent(event);

        if (file == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=" + "dokumenty_oa_bk" + event.getEventId() + ".fileGenerator");
        headers.setContentLength(file.length);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    private byte[] createBkForEvent(Event event) {
        List<EventTeam> teams = event.getEventTeams();

        try {
            createPagesForTeams(teams);
            return getPdf(event, teams);
        } catch (IOException e) {
            LOGGER.error("IOException" + e.getMessage());
            e.printStackTrace();
        } catch (DocumentException e) {
            LOGGER.error("DocumentException" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void createPagesForTeams(List<EventTeam> eventTeams) throws IOException, DocumentException {
        Document doc = new Document();
        PdfReader reader = new PdfReader(BK_FORM_PATH);
        PdfCopy copy = new PdfSmartCopy(doc, new FileOutputStream(BK_FOR_EVENT_FORM_PATH));
        doc.open();
        for (int i = 1; i <= eventTeams.size(); i++)
            copy.addPage(copy.getImportedPage(reader, 1));
        doc.close();
        copy.close();
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
                if (event.getLogoPathFile() != null) addLogoImage(over, event.getLogoPathFile());
                if (Boolean.FALSE.equals(event.getPzm())) hidePzm(over);
            }
            stamper.close();
            reader.close();
            return baos.toByteArray();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addLogoImage(PdfContentByte over, byte[] logoPathFile) {
        try {
            Image img = Image.getInstance(logoPathFile);
            img.scaleToFit(100, 50);
            img.setAbsolutePosition(470, 780 + (50 - img.getScaledHeight()) / 2);
            over.addImage(img);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void hidePzm(PdfContentByte over) {
        Rectangle rectangle = new Rectangle(50, 780, 150, 840);
        rectangle.setBackgroundColor(BaseColor.WHITE);

        over.rectangle(rectangle);
    }

    private void fillData(PdfContentByte over, Event event, EventTeam et, BaseFont bf) {
        over.beginText();
        over.setFontAndSize(bf, 12);
        over.setTextMatrix(NUMBER_X, NAME_ROW_Y);
        over.showText(et.getNumber().toString());
        over.setTextMatrix(CLASS_X, NAME_ROW_Y);
        over.showText(et.getCarClass().getName());

        over.setTextMatrix(DRIVER_X, NAME_ROW_Y);
        over.showText(et.getDriver());

        over.setTextMatrix(CO_DRIVER_X, NAME_ROW_Y);
        over.showText(et.getCoDriver());

        fillEventInfo(over, event, bf);
        over.setFontAndSize(bf, 12);

        over.setTextMatrix(CAR_1_X, CAR_1_Y);
        over.showText(et.getCar().getBrand());
        over.setTextMatrix(CAR_1_X, CAR_2_Y);
        over.showText(et.getCar().getModel());
        over.setTextMatrix(CAR_1_X, CAR_3_Y);
        String engine = et.getCar().getEngineCapacity().toString();
        over.showText(engine.substring(0, engine.indexOf('.')));

        over.setTextMatrix(CAR_2_X, CAR_1_Y);
        over.showText(et.getCar().getLicensePlate().toUpperCase());
        over.setTextMatrix(CAR_2_X, CAR_2_Y);
        over.showText(et.getCar().getVin().toUpperCase());

        fillTurboInfo(over, et.getCar().getTurbo());

        over.setFontAndSize(bf, 10);
        over.setTextMatrix(DRIVING_LICENSE_X, DRIVING_LICENSE_Y);
        over.showText(et.getTeam().getDrivingLicense());

        over.setTextMatrix(DRIVING_LICENSE_X, DRIVING_LICENSE_Y - 19);
        over.showText(et.getTeam().getCoDrivingLicense());

        over.setTextMatrix(DRIVING_LICENSE_X, DRIVING_LICENSE_Y - 58);
        over.showText(et.getCar().getInsurance());
        over.setTextMatrix(DRIVING_LICENSE_X, DRIVING_LICENSE_Y - 76);
        over.showText(et.getCar().getInsurance());

        over.endText();
    }

    private void fillEventInfo(PdfContentByte over, Event event, BaseFont bf) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withZone(ZoneId.systemDefault());

        String eventName = event.getName();
        int fontSize = 10;
        if (eventName.length() > 32)
            fontSize = 8;
        if (eventName.length() > 42)
            fontSize = 6;
        over.setFontAndSize(bf, fontSize);
        over.setTextMatrix(EVENT_X, NAME_ROW_Y + 6);
        over.showText(event.getName());

        over.setTextMatrix(EVENT_X + 80, NAME_ROW_2_Y);
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
