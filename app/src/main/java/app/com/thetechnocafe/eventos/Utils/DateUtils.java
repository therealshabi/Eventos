package app.com.thetechnocafe.eventos.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.format;

/**
 * Created by gurleensethi on 23/11/16.
 */

public class DateUtils {

    public static SimpleDateFormat mformat;
    /**
     * Convert date to required string
     */
    public static String getFormattedDate(Date date) {
        mformat = new SimpleDateFormat("dd MMM yyyy");
        return mformat.format(date);
    }

    /**
     * Convert date to time format ( 8:00 pm)
     */
    public static String convertToTime(Date date) {
        mformat = new SimpleDateFormat("hh:mm a");
        return mformat.format(date);
    }


}
