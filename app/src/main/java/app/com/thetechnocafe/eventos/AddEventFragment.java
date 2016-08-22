package app.com.thetechnocafe.eventos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddEventFragment extends Fragment {
    private static final String TAG = "AddEventFragment";
    private static final String DATE_PICKER_TAG = "datepicker";
    private static final String TIME_PICKER_TAG = "timepicker";
    private static final int DATE_PICKER_CODE = 1;
    private static final int TIME_PICKER_CODE = 2;

    private TextView mInfoText;
    private ImageButton mAddContactImageButton;
    private LinearLayout mContactsContainer;
    private ImageButton mAddForumLinkImageButton;
    private LinearLayout mLinkContainer;
    private TextView mDateText;
    private TextView mTimeText;
    private Date mEventDate;
    private Date mEventDateTime;

    public static AddEventFragment getInstance() {
        return new AddEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        mInfoText = (TextView) view.findViewById(R.id.fragment_add_event_info);
        mAddContactImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_contact);
        mContactsContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_contacts_container);
        mAddForumLinkImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_forum_link);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_forum_link_container);
        mDateText = (TextView) view.findViewById(R.id.fragment_add_event_date);
        mTimeText = (TextView) view.findViewById(R.id.fragment_add_event_time);

        //Set up date and text
        mEventDate = new GregorianCalendar().getTime();
        setDateText(mEventDate);

        //Set up time text
        mEventDateTime = new GregorianCalendar().getTime();
        setTimeText(mEventDateTime);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_add_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Set up info text with image view in it
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Drawable textImage = getResources().getDrawable(R.drawable.ic_show_info);
        if (textImage != null) {
            textImage.setBounds(0, 0, mInfoText.getLineHeight(), mInfoText.getLineHeight());
        }
        builder.append(getString(R.string.new_event_info))
                .append(" ")
                .setSpan(new ImageSpan(textImage), builder.length() - 1, builder.length(), 0);
        mInfoText.setText(builder);

        //Set up on click for add contact image button (add new view to linear layout contacts container)
        mAddContactImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToContainer();
            }
        });

        //Set up on click for add forum link image button (add new view to linear layout link container)
        mAddForumLinkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addForumLinkToContainer();
            }
        });

        //Set up on click listener for date text view (fire up DialogDatePicker)
        mDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.getInstance(mEventDate);
                dialogDatePicker.setTargetFragment(AddEventFragment.this, DATE_PICKER_CODE);
                dialogDatePicker.show(getFragmentManager(), DATE_PICKER_TAG);
            }
        });

        //Set up on click listener for time text view (fire up DialogTimePicker)
        mTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTimePicker dialogTimePicker = DialogTimePicker.getInstance(mEventDateTime);
                dialogTimePicker.setTargetFragment(AddEventFragment.this, TIME_PICKER_CODE);
                dialogTimePicker.show(getFragmentManager(), TIME_PICKER_TAG);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_new_event_menu, menu);
    }

    private void addContactToContainer() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_email_phone, null);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.contact_email_phone_remove);
        imageButton.setVisibility(View.VISIBLE);
        //Set up remove function for remove view
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactsContainer.removeView(view);
            }
        });
        mContactsContainer.addView(view);
    }

    private void addForumLinkToContainer() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.submission_forum, null);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.submission_forum_remove_button);
        imageButton.setVisibility(View.VISIBLE);
        //Set up remove function to remove view
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinkContainer.removeView(view);
            }
        });
        mLinkContainer.addView(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == DATE_PICKER_CODE && data != null) {
                mEventDate = DialogDatePicker.getDate(data);
                setDateText(mEventDate);
            } else if (requestCode == TIME_PICKER_CODE && data != null) {
                mEventDateTime = DialogTimePicker.getTime(data);
                setTimeText(mEventDateTime);
            }
        }
    }

    //Function to set the date text
    private void setDateText(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateText = simpleDateFormat.format(date);
        mDateText.setText(dateText);
    }

    //Function to set time text
    private void setTimeText(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String timeText = simpleDateFormat.format(date);
        mTimeText.setText(timeText);
    }
}
