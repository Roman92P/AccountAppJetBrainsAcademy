package account.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateConvert {

    public static String convertDigitMonthToString(String s) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MM-yyyy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        LocalDate date = LocalDate.parse(s, formatter);
        String mthStr = date.getMonth().toString();
        String month = mthStr.charAt(0) + mthStr.toLowerCase().substring(1);
       return month + "-" + date.getYear();
    }
}
