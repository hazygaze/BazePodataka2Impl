package sample.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Konstante {
    public static final int INSERT = 1;
    public static final int EDIT = 2;

    public static final String FORMAT = "dd.MM.yyyy";


    public static String vratiFormatiranDatum(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);

    }


}
