package com.akbp.racescore.service;

import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.repository.EventRepository;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
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
public class FileService {

    @Autowired
    private EventRepository eventRepository;

    public ResponseEntity<byte[]> getEventTeamsData(Long eventId) throws IOException {

        Event event = eventRepository.getById(eventId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        try {
            FontProgram fontProgram = FontProgramFactory.createFont();
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
        doc.add(createTitle(event));
        doc.add(createPersonalDataTable(et.getTeam()));
        doc.add(new Paragraph("\n\n Dane samochodu:"));
        doc.add(createCarDataTable(et.getTeam().getCurrentCar()));
        doc.add(createApprovalParagraph(event));
        doc.add(createSignSpace());
        doc.add(new AreaBreak());
    }

    private Table createPersonalDataTable(Team team) {
        Table table = new Table(3).setWidth(500);

        table.addCell("            ");
        table.addCell("Kierowca    ").setBold();
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

        return table;
    }

    private Table createCarDataTable(Car car) {
        Table table = new Table(4).setWidth(500);

        if (car == null) return table;

        table.addCell("Marka");
        table.addCell(Optional.ofNullable(car.getBrand()).orElse(""));
        table.addCell("Model");
        table.addCell(Optional.ofNullable(car.getModel()).orElse(""));
        table.addCell("Nr rej.");
        table.addCell(Optional.ofNullable(car.getLicensePlate()).orElse(""));
        table.addCell("Rok produkcji");
        table.addCell(Optional.ofNullable(car.getYear()).orElse(""));
        table.addCell("Pojemność skokowa");
        table.addCell(Optional.ofNullable(car.getEngineCapacity()).orElse(Double.valueOf(0)).intValue() + " cm3");
        table.addCell("Turbo");
        table.addCell(Boolean.TRUE.equals(car.getTurbo()) ? "TAK" : "NIE");

        table.addCell("Nr VIN");
        Cell vin = new Cell(1, 3).add(new Paragraph(Optional.ofNullable(car.getVin()).orElse("")));
        table.addCell(vin);

        table.addCell("Data ważności przeglądu");
        table.addCell(car.getCarInspectionExpiryDate() == null ? "" : fromInstant(car.getCarInspectionExpiryDate()));
        table.addCell("Data ważności polisy");
        table.addCell(car.getInsuranceExpiryDate() == null ? "" : fromInstant(car.getInsuranceExpiryDate()));

        table.addCell("Nr polisy oraz firma");
        Cell insurance = new Cell(1, 3).add(new Paragraph(Optional.ofNullable(car.getInsurance()).orElse("")));
        table.addCell(insurance);

        return table;
    }

    private Paragraph createTitle(Event event) {

        Paragraph p = new Paragraph(event.getName() + "\n").setFontSize(14).setTextAlignment(TextAlignment.CENTER);
        p.add(fromInstant(event.getDate()) + "\n");
        p.add("Organizator: " + event.getOrganizer());
        p.add("\n\n\n");

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
