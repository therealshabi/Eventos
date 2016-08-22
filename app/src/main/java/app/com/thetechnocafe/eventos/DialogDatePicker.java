package app.com.thetechnocafe.eventos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gurleensethi on 22/08/16.
 */
public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String DATE_TAG = "datetag";
    private static final String DATE_MONTH_TAG = "datemonth";
    private static final String DATE_DAY_TAG = "dateday";
    private static final String DATE_YEAR_TAG = "dateyear";
    private static Date mSelectedDate;

    public static DialogDatePicker getInstance(Date date) {
        DialogDatePicker dialogDatePicker = new DialogDatePicker();
        mSelectedDate = date;
        return dialogDatePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mSelectedDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Intent intent = new Intent();
        intent.putExtra(DATE_DAY_TAG, dayOfMonth);
        intent.putExtra(DATE_MONTH_TAG, monthOfYear);
        intent.putExtra(DATE_YEAR_TAG, year);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    public static Date getDate(Intent intent) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(mSelectedDate);
        calendar.set(intent.getIntExtra(DATE_YEAR_TAG, 0), intent.getIntExtra(DATE_MONTH_TAG, 0), intent.getIntExtra(DATE_DAY_TAG, 0));
        return calendar.getTime();
    }
}
