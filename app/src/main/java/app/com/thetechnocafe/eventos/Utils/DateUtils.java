package app.com.thetechnocafe.eventos.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gurleensethi on 23/11/16.
 */

public class DateUtils {
    /**
     * Convert date to required string
     */
    public static String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        return format.format(date);
    }

    /**
     * Convert date to time format ( 8:00 pm)
     */
    public static String convertToTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }


}
