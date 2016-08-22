package app.com.thetechnocafe.eventos;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by gurleensethi on 22/08/16.
 */
public class DialogTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG_HOUR = "hour";
    private static final String TAG_MINUTE = "minute";
    private static Date mSelectedDate;

    public static DialogTimePicker getInstance(Date date) {
        DialogTimePicker dialogTimePicker = new DialogTimePicker();
        mSelectedDate = date;
        return dialogTimePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mSelectedDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent();
        intent.putExtra(TAG_HOUR, hourOfDay);
        intent.putExtra(TAG_MINUTE, minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    public static Date getTime(Intent intent) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, intent.getIntExtra(TAG_HOUR, 0));
        calendar.set(Calendar.MINUTE, intent.getIntExtra(TAG_MINUTE, 0));
        return calendar.getTime();
    }
}
