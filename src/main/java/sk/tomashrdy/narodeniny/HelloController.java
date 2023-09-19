package sk.tomashrdy.narodeniny;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    SpravcaOsob spravcaOsob = new SpravcaOsob();

    @FXML
    private ListView<Osoba> osobyListView;
    @FXML
    private Label dnesLabel;
    @FXML
    private Label najblizsiLabel;
    @FXML
    private Label narodeninyLabel;
    @FXML
    private Label vekLabel;

    @FXML
    public void handlePridatButtonAction(ActionEvent event) {
        Dialog<Osoba> dialog = new Dialog<>();
        dialog.setTitle("Nová osoba");
        dialog.setWidth(350);
        dialog.setHeight(250);

        vytvorObsahDialogu(dialog);
        vytvorObsahDialogu(dialog);
        final Optional<Osoba> vysledok = dialog.showAndWait();
        if (vysledok.isPresent()) {
            Osoba osoba = vysledok.get();
            spravcaOsob.pridaj(osoba);
        }
    }

    @FXML
    public void handleOdobratButtonAction(ActionEvent event) {

        Osoba vybrana = osobyListView.getSelectionModel().getSelectedItem();
        if (vybrana != null) {
            spravcaOsob.odober(vybrana);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dnesLabel.setText(Datum.datumNaText(Calendar.getInstance()));
        osobyListView.setItems(spravcaOsob.getOsoby());
        if (!osobyListView.getItems().isEmpty()){
            osobyListView.getSelectionModel().select(0);
        }
        osobyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                obnov();
            }
        });
    }

    public void vytvorObsahDialogu(Dialog<Osoba> dialog){

        ButtonType createButtonType = new ButtonType("OK" , ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().setAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField menoTextField = new TextField();
        TextField datumTextField = new TextField();
        Label menoLabel = new Label("Meno");
        Label datumLabel = new Label("Dátum narodenia");

        grid.add(menoLabel,0,0);
        grid.add(menoTextField,1,0);
        grid.add(datumLabel,0,1);
        grid.add(datumTextField,1,1);

        dialog.setResultConverter(new Callback<ButtonType, Osoba>() {
            @Override
            public Osoba call(ButtonType param) {
                try {
                    Calendar narodeniny = Datum.textNaDatum(datumTextField.getText());
                    return new Osoba(menoTextField.getText(), narodeniny);
                } catch (ParseException | IllegalArgumentException ex) {
                    System.out.println("Chyba: " + ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Osobu se nepodarilo nastaviť!");
                    alert.showAndWait();
                    return null;
                }
            }
        });
        dialog.getDialogPane().setContent(grid);
    }

    public void obnov(){
        Osoba vybranaOsoba = osobyListView.getSelectionModel().getSelectedItem();
        if (vybranaOsoba != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy");
            String narodeninyText = sdf.format(vybranaOsoba.getNarodeniny().getTime());
            narodeninyLabel.setText(narodeninyText);
            vekLabel.setText(calculateAge(narodeninyText));
            najblizsiLabel.setText(daysUntilBirthday(narodeninyText));
        } else {
            vekLabel.setText("Žiadna osoba nie je vybraná");
        }
    }
    public static String calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            return String.valueOf(age);
        } catch (DateTimeParseException e) {
            System.err.println("Nesprávny formát dátumu: " + e.getMessage());
            return "Chyba pri výpočte veku"; // alebo akúkoľvek inú chybovú správu
        }
    }
    public static String daysUntilBirthday(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
            LocalDate today = LocalDate.now();

            // Ajustujeme rok narodenín na aktuálny alebo nasledujúci rok
            LocalDate nextBirthday = birthDate.withYear(today.getYear());
            if (today.isAfter(nextBirthday) || today.isEqual(nextBirthday)) {
                nextBirthday = nextBirthday.plusYears(1);
            }

            long daysUntilBirthday = ChronoUnit.DAYS.between(today, nextBirthday);
            return String.valueOf(daysUntilBirthday);
        } catch (DateTimeParseException e) {
            System.err.println("Nesprávny formát dátumu: " + e.getMessage());
            return "Chyba pri výpočte dní do narodenín"; // alebo akúkoľvek inú chybovú správu
        }
    }
}