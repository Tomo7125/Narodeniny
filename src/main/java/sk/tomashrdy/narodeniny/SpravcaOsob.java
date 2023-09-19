package sk.tomashrdy.narodeniny;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SpravcaOsob {

    private ObservableList<Osoba> osoby = FXCollections.observableArrayList();

    public ObservableList<Osoba> getOsoby() {
        return osoby;
    }

    public void pridaj(Osoba osoba){
        osoby.add(osoba);
    }
    public void odober(Osoba osoba){
        osoby.remove(osoba);
    }

}
