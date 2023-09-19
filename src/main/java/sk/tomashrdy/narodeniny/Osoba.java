package sk.tomashrdy.narodeniny;

import java.util.Calendar;

public class Osoba {

    private String meno;
    private Calendar narodeniny;

    public String getMeno() {
        return meno;
    }

    public Calendar getNarodeniny() {
        return narodeniny;
    }


    public Osoba(String meno, Calendar narodeniny) throws IllegalArgumentException{

        Datum.nastavPolnoc(narodeniny);
        if (meno.length() < 3)
            throw new IllegalArgumentException("Meno je krátke");

        if (narodeniny.after(Calendar.getInstance()))
            throw new IllegalArgumentException("Datum nesmie byť v buducnosti");

        this.meno = meno;
        this.narodeniny = narodeniny;
    }

    @Override
    public String toString() {
        return meno;
    }

}
