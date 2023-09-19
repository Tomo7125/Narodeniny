package sk.tomashrdy.narodeniny;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Datum {

    private static DateFormat formatData = new SimpleDateFormat("d.M.yyyy");

    public static void nastavPolnoc(Calendar datum) {
        datum.set(Calendar.HOUR_OF_DAY, 0);
        datum.set(Calendar.MINUTE, 0);
        datum.set(Calendar.SECOND, 0);
        datum.set(Calendar.MILLISECOND, 0);
    }

    public static String datumNaText(Calendar datum) {
        String datumText = formatData.format(datum.getTime());
        return datumText;
    }

    public static Calendar textNaDatum(String datumText) throws ParseException {
        Calendar datum = Calendar.getInstance();
        datum.setTime(formatData.parse(datumText));
        return datum;
    }

}
