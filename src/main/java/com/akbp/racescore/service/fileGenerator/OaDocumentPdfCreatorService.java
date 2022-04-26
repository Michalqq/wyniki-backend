package com.akbp.racescore.service.fileGenerator;

import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.EventRepository;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OaDocumentPdfCreatorService {

    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity<byte[]> getEventTeamsData(Long eventId) throws IOException {
        Event event = eventRepository.getById(eventId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        try {
            FontProgram fontProgram = FontProgramFactory.createFont(StandardFonts.HELVETICA);
            PdfFont font = PdfFontFactory.createFont(fontProgram, "CP1250");
            doc.setFont(font).setFontSize(10);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        for (EventTeam et : event.getEventTeams().stream().sorted(Comparator.comparingInt(x -> x.getNumber())).collect(Collectors.toList()))
            createPage(doc, et, event);

        doc.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=" + "Odbior_Administracyjny_" + event.getEventId());
        headers.setContentLength(out.toByteArray().length);

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    private void createPage(Document doc, EventTeam et, Event event) {
        doc.add(createNumberBox(et.getNumber()));
        doc.add(createTitle(event));
        doc.add(new Paragraph("Dane zawodników:"));
        Table personalDataTable = createPersonalDataTable(et.getTeam());
        doc.add(personalDataTable);
        personalDataTable.complete();
        doc.add(new Paragraph("\n\n Dane samochodu:"));
        Table carDataTable = createCarDataTable(et.getTeam().getCurrentCar());
        doc.add(carDataTable);
        carDataTable.complete();
        doc.add(createApprovalParagraph(event));
        doc.add(createSignSpace());
        doc.add(new AreaBreak());
    }

    private Table createNumberBox(Integer number) {
        Table box = new Table(1).setHorizontalAlignment(HorizontalAlignment.RIGHT);
        box.addCell("Numer startowy");

        Paragraph p = new Paragraph(number.toString()).setFontSize(18).setTextAlignment(TextAlignment.CENTER);

        box.getCell(0, 0).setMarginLeft(500)
                .setPaddingBottom(20).setPadding(5)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(p);

        return box;
    }

    private Table createPersonalDataTable(Team team) {
        float[] columnWidths = new float[]{13f, 20f, 20f};
        Table table = new Table(columnWidths, true);

        table.addCell("            ");
        table.addCell("Kierowca    ");
        table.addCell("Pilot       ");

        table.addCell("Imię i nazwisko").setProperty(8, false);
        table.addCell(Optional.ofNullable(team.getDriver()).orElse(""));
        table.addCell(Optional.ofNullable(team.getCoDriver()).orElse(""));

        table.addCell("Adres");
        table.addCell("");
        table.addCell("");

        table.addCell("Data urodzenia");
        table.addCell(team.getBirthDate() == null ? "" : fromInstant(team.getBirthDate()));
        table.addCell(team.getCoBirthDate() == null ? "" : fromInstant(team.getCoBirthDate()));

        table.addCell("Telefon");
        table.addCell(Optional.ofNullable(team.getPhone()).orElse(""));
        table.addCell(Optional.ofNullable(team.getCoPhone()).orElse(""));

        table.addCell("Email");
        table.addCell(Optional.ofNullable(team.getEmail()).orElse(""));
        table.addCell(Optional.ofNullable(team.getCoEmail()).orElse(""));

        table.addCell("Prawo jazdy");
        table.addCell(Optional.ofNullable(team.getDrivingLicense()).orElse(""));
        table.addCell(Optional.ofNullable(team.getCoDrivingLicense()).orElse(""));

        table.addCell("Klub");
        table.addCell(Optional.ofNullable(team.getClub()).orElse(""));
        table.addCell(Optional.ofNullable(team.getCoClub()).orElse(""));

        table.addCell("Licencja sportu samoch.");
        table.addCell(Boolean.TRUE.equals(team.getSportLicense()) ? "TAK" : "NIE");
        table.addCell(Boolean.TRUE.equals(team.getCoSportLicense()) ? "TAK" : "NIE");

        setBold(table);

        return table;
    }

    private void setBold(Table table) {
        table.getCell(0, 1).setBold();
        table.getCell(0, 2).setBold();

        for (int i = 0; i < 9; i++) {
            table.getCell(i, 0).setBold();
        }
    }

    private Table createCarDataTable(Car car) {
        float[] columnWidths = new float[]{15f, 15f, 15f, 15f};
        Table table = new Table(columnWidths, true);

        if (car == null) return table;

        table.addCell("Marka");
        table.addCell(Optional.ofNullable(car.getBrand()).orElse(""));
        table.addCell("Model");
        table.addCell(Optional.ofNullable(car.getModel()).orElse(""));
        table.addCell("Nr rej.");
        table.addCell(Optional.ofNullable(car.getLicensePlate()).map(x -> x.toUpperCase()).orElse(""));
        table.addCell("Rok produkcji");
        table.addCell(Optional.ofNullable(car.getYear()).orElse(""));
        table.addCell("Pojemność skokowa");
        table.addCell(Optional.ofNullable(car.getEngineCapacity()).orElse(Double.valueOf(0)).intValue() + " cm3");
        table.addCell("Turbo");
        table.addCell(Boolean.TRUE.equals(car.getTurbo()) ? "TAK" : "NIE");

        table.addCell("Nr VIN");
        Cell vin = new Cell(1, 3).add(new Paragraph(Optional.ofNullable(car.getVin()).map(x -> x.toUpperCase()).orElse("")));
        table.addCell(vin);

        table.addCell("Data ważności przeglądu");
        table.addCell(car.getCarInspectionExpiryDate() == null ? "" : fromInstant(car.getCarInspectionExpiryDate()));
        table.addCell("Data ważności polisy");
        table.addCell(car.getInsuranceExpiryDate() == null ? "" : fromInstant(car.getInsuranceExpiryDate()));

        table.addCell("Nr polisy oraz firma");
        Cell insurance = new Cell(1, 3).add(new Paragraph(Optional.ofNullable(car.getInsurance()).orElse("")));
        table.addCell(insurance);

        setCarTableBold(table);

        return table;
    }

    private void setCarTableBold(Table table) {
        for (int i = 0; i < 6; i++) {
            Cell cell = table.getCell(i, 0);
            if (cell != null)
                cell.setBold();

            Cell cell2 = table.getCell(i, 2);
            if (cell2 != null)
                cell2.setBold();
        }
    }

    private Paragraph createTitle(Event event) {
        Paragraph p = new Paragraph(event.getName() + "\n").setFontSize(14).setPaddingLeft(80).setPaddingRight(80).setTextAlignment(TextAlignment.CENTER);
        p.setMarginTop(-40);
        p.add(fromInstant(event.getDate()) + "\n");
        p.add("Organizator: " + event.getOrganizer());
        p.add("\n\n");

        return p;
    }

    private Table createSignSpace() {
        Table table = new Table(2).setWidth(500).setBorder(Border.NO_BORDER);

        table.addCell("Podpis kierowcy:");
        table.addCell("Podpis pilota:");

        return table;
    }

    private Paragraph createApprovalParagraph(Event event) {
        Paragraph p = new Paragraph().setFontSize(8).setVerticalAlignment(VerticalAlignment.BOTTOM);
        p.add("\n\n\n");
        p.add("Zgłaszamy swój udział w imprezie samochodowej i stwierdzamy podpisami prawdziwość danych zawartych w " +
                "zgłoszeniu.\n" +
                "Oświadczamy, że jesteśmy świadomi ryzyka i niebezpieczeństwa podczas imprezy. W związku z moim uczestnictwem " +
                "przyjmujemy na siebie pełną odpowiedzialność, jednocześnie zrzekamy się wszelkich późniejszych roszczeń w " +
                "stosunku do organizatora. Oświadczamy, że znany jest nam Regulamin Imprezy i Regulamin Uzupełniający oraz " +
                "przepisy w nich zawarte i zobowiązujemy się do ich przestrzegania.");

        p.add("Wyrażam zgodę na przetwarzanie moich danych osobowych zawartych w powyższym formularzu w celu " +
                "przygotowania i przeprowadzania imprezy, w tym do prezentacji ich na liście zgłoszeń, na liście startowej, w wynikach " +
                "imprezy oraz w materiałach informacyjnych imprezy. Uczestnik ma prawo wglądu, edycji oraz usunięcia swoich danych " +
                "poprzez kontakt z biurem organizatora imprezy. Wyrażam zgodę na publikację zdjęć z moim udziałem z " +
                "imprezy w której uczestniczę na stronie internetowej i profilach społecznościowych organizatora: " + event.getOrganizer() +
                " i Polskiego Związku Motorowego. Niniejszym wyrażam zgodę na przetwarzanie przez " + event.getOrganizer() +
                " i Polski Związek Motorowy moich danych osobowych zgodnie z ustawą o ochronie" +
                "danych osobowych. (Dz. U. Nr 133/97, poz. 883)");
        p.add("\n\n\n");
        return p;
    }

    private String fromInstant(Instant date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault());
        return formatter.format(date);
    }
}
