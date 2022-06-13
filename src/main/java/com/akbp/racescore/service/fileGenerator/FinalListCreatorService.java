package com.akbp.racescore.service.fileGenerator;

import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Stage;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.service.StatementService;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FinalListCreatorService {

    private final StatementService statementService;

    private Instant startTime;
    private Long frequency;

    public byte[] createFinalListFile(Event event, Stage stage, List<EventTeam> eventTeams, Instant startTime, Long frequency) throws IOException {
        this.startTime = startTime;
        this.frequency = frequency;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        try {
            FontProgram fontProgram = FontProgramFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont font = PdfFontFactory.createFont(fontProgram, "CP1250");
            doc.setFont(font).setFontSize(6);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        createPage(doc, eventTeams, event, stage);

        doc.close();

        return out.toByteArray();
    }

    private void createPage(Document doc, List<EventTeam> eventTeams, Event event, Stage stage) {
        Image img = addLogo(event.getLogoPathFile());
        if (img != null) doc.add(img);

        doc.add(createTitle(event, stage));
        Table table = createFinalListTable(eventTeams);
        setAlign(table);
        styleHeader(table);
        doc.add(table);
        table.complete();
    }

    private void styleHeader(Table table) {
        table.getHeader().setBold().setFontSize(7).setBackgroundColor(ColorConstants.LIGHT_GRAY);
    }

    private Table createFinalListTable(List<EventTeam> eventTeams) {
        float[] columnWidths = new float[]{2f, 2f, 12f, 10f, 4f, 7f, 10f, 4f};
        Table table = new Table(columnWidths, true);

        table.addHeaderCell("L.p");
        table.addHeaderCell("Nr");
        table.addHeaderCell("Załoga");
        table.addHeaderCell("Automobilklub");
        table.addHeaderCell("Klasa");
        table.addHeaderCell("Samochód");
        table.addHeaderCell("Team");
        table.addHeaderCell("Czas startu");

        eventTeams = eventTeams.stream().sorted(Comparator.comparingLong(EventTeam::getOrder)).collect(Collectors.toList());

        int count = 1;
        for (EventTeam et : eventTeams) {
            Team team = et.getTeam();
            table.addCell(String.valueOf(count++)).setBorder(Border.NO_BORDER);
            table.addCell(et.getNumber().toString());
            table.addCell(getTeamNames(et));
            table.addCell(getClubs(et));
            table.addCell(Optional.ofNullable(et.getCarClass().getName()).orElse(""));
            table.addCell(Optional.ofNullable(et.getCar().getBrand()).orElse("") + " "
                    + Optional.ofNullable(et.getCar().getModel()).orElse(""));
            table.addCell(Optional.ofNullable(team.getTeamName()).orElse(""));
            table.addCell(getTime());
        }

        return table;
    }

    private String getTime() {
        int hour = startTime.atZone(ZoneOffset.UTC).getHour();
        int minutes = startTime.atZone(ZoneOffset.UTC).getMinute();

        startTime = startTime.plusSeconds(60 * frequency);

        return hour + ":" + (minutes < 10 ? "0" + minutes : minutes);
    }

    private void setAlign(Table table) {
        table.getCell(1, 1).setHorizontalAlignment(HorizontalAlignment.CENTER);
    }

    private String getTeamNames(EventTeam et) {
        String coDriver = et.getCoDriver() != null && et.getCoDriver() != "" ? " / " + et.getCoDriver() : "";
        return Optional.ofNullable(et.getDriver()).map(x -> x + coDriver).orElse("");
    }

    private String getClubs(EventTeam et) {
        String coClub = et.getCoClub() != null && et.getCoClub() != "" ? " / " + et.getCoClub() : "";
        return Optional.ofNullable(et.getClub()).map(x -> x + coClub).orElse("");
    }

    private Image addLogo(byte[] logoPathFile) {
        if (logoPathFile == null) return null;

        ImageData imageData = ImageDataFactory.create(logoPathFile);
        Image img = new Image(imageData);

        img.setMaxWidth(100);
        img.setMaxHeight(100);

        img.setFixedPosition(25, 740);

        return img;
    }

    private Paragraph createTitle(Event event, Stage stage) {
        Paragraph p = new Paragraph(event.getName() + "\n").setFontSize(12).setPaddingLeft(80).setPaddingRight(80).setTextAlignment(TextAlignment.CENTER);
        p.setMarginTop(-10);
        p.add(fromInstant(event.getDate()) + "\n");
        p.add("Organizator: " + event.getOrganizer());
        p.add("\n\n");
        p.add("Lista startowa na odcinek: " + stage.getName());

        return p;
    }

    private String fromInstant(Instant date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault());
        return formatter.format(date);
    }
}
