package app.com.thetechnocafe.eventos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Dialogs.DialogDatePicker;
import app.com.thetechnocafe.eventos.Dialogs.DialogTimePicker;
import app.com.thetechnocafe.eventos.Dialogs.LoadingDialog;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddEventFragment extends Fragment {
    private static final String TAG = "AddEventFragment";
    private static final String DATE_PICKER_TAG = "datepicker";
    private static final String TIME_PICKER_TAG = "timepicker";
    private static final String GUIDELINES_TAG = "guidlines";
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
    private TextView mAddCategoryButton;
    private Button mSubmitButton;
    private RequestUtils mRequestUtils;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mRequirementsEditText;
    private EditText mImageEditText;
    private EditText mVenueEditText;
    private LoadingDialog mLoadingDialog;

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
        final View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        mInfoText = (TextView) view.findViewById(R.id.fragment_add_event_info);
        mAddContactImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_contact);
        mContactsContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_contacts_container);
        mAddForumLinkImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_forum_link);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_forum_link_container);
        mDateText = (TextView) view.findViewById(R.id.fragment_add_event_date);
        mTimeText = (TextView) view.findViewById(R.id.fragment_add_event_time);
        mAddCategoryButton = (TextView) view.findViewById(R.id.fragment_add_event_avatar);
        mSubmitButton = (Button) view.findViewById(R.id.fragment_add_event_submit);
        mTitleEditText = (EditText) view.findViewById(R.id.fragment_add_event_title);
        mDescriptionEditText = (EditText) view.findViewById(R.id.fragment_add_event_description);
        mVenueEditText = (EditText) view.findViewById(R.id.fragment_add_event_venue);
        mImageEditText = (EditText) view.findViewById(R.id.fragment_add_event_image_link);
        mRequirementsEditText = (EditText) view.findViewById(R.id.fragment_add_event_requirement);

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

        mRequestUtils = new RequestUtils() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful) {
                //TODO:Close the dialog box after form is submitted
                //If successful the finish the activity
                if (isSuccessful) {
                    //Show Toast message and finish the activity
                    Toast.makeText(getContext(), getString(R.string.submission_success), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    //Notify user about error
                    if (isAdded()) {
                        Snackbar.make(view, getString(R.string.forum_submission_error), Snackbar.LENGTH_SHORT).show();
                    }
                }

                mLoadingDialog.dismiss();
            }
        };

        //Set on click listeners
        setOnClickListeners();

        return view;
    }

    private void setOnClickListeners() {
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View categoryView = LayoutInflater.from(getContext()).inflate(R.layout.category_dialog, null);
                builder.setView(categoryView);
                AlertDialog categoryDialog = builder.create();
                categoryDialog.show();
            }
        });

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

        //Set on click listener for submit button
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForum()) {
                    //Show loading dialog
                    mLoadingDialog = LoadingDialog.getInstance(getString(R.string.submitting_form));
                    mLoadingDialog.show(getFragmentManager(), "");

                    JSONObject object = getJSONObjectFromFormData();
                    mRequestUtils.submitForumToServer(getContext(), object);

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
            case R.id.add_new_event_menu_guidelines: {
                GuidelinesDialog guidelinesDialog = GuidelinesDialog.getInstance();
                guidelinesDialog.show(getFragmentManager(), GUIDELINES_TAG);
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DATE_PICKER_CODE && data != null) {
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

    //Validate forum before submission
    private boolean validateForum() {
        String errorString = "";
        boolean isValid = true;

        //Validate form
        if (mTitleEditText.getText().toString().length() < 15) {
            //Check title
            mTitleEditText.requestFocus();
            errorString = getString(R.string.form_error_title);
            isValid = false;
        } else if (mDescriptionEditText.getText().toString().length() < 50) {
            //Check description
            mDescriptionEditText.requestFocus();
            errorString = getString(R.string.form_error_description);
            isValid = false;
        } else if (mVenueEditText.getText().toString().length() < 2) {
            //Check venue
            mVenueEditText.requestFocus();
            errorString = getString(R.string.form_error_venue);
            isValid = false;
        } else if (mImageEditText.getText().toString().length() == 0) {
            //Check image
            mImageEditText.requestFocus();
            errorString = getString(R.string.form_error_image);
            isValid = false;
        }

        //Check if form passed validation
        if (!isValid) {
            Snackbar.make(getView(), errorString, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Convert the forum data to JSON object
    private JSONObject getJSONObjectFromFormData() {
        JSONObject object = new JSONObject();

        try {
            object.put(StringUtils.JSON_TITLE, mTitleEditText.getText().toString());
            object.put(StringUtils.JSON_DESCRIPTION, mDescriptionEditText.getText().toString());
            object.put(StringUtils.JSON_VENUE, mVenueEditText.getText().toString());
            object.put(StringUtils.JSON_IMAGE, mImageEditText.getText().toString());
            object.put(StringUtils.JSON_AVATAR_ID, 0);
            object.put(StringUtils.JSON_REQUIREMENTS, mRequirementsEditText.getText().toString());

            //Get links list
            JSONArray list = getLinksList();
            if (list != null) {
                object.put(StringUtils.JSON_LINKS, list);
            }
        } catch (JSONException e) {
            mRequestUtils.isRequestSuccessful(false);
        }

        return object;
    }

    private JSONArray getLinksList() {
        JSONArray jsonArray = new JSONArray();

        //Traverse the childs
        for (int count = 0; count < mLinkContainer.getChildCount(); count++) {
            View view = mLinkContainer.getChildAt(count);

            EditText mNameEditText = (EditText) view.findViewById(R.id.submission_forum_item_name);
            EditText mLinkEditText = (EditText) view.findViewById(R.id.submission_forum_item_link);

            if (!mNameEditText.getText().toString().equals("") && !mLinkEditText.getText().toString().equals("")) {
                //Create a json object
                JSONObject object = new JSONObject();
                try {
                    object.put(StringUtils.JSON_LINKS_NAME, mNameEditText.getText().toString());
                    object.put(StringUtils.JSON_LINKS_ADDRESS, mLinkEditText.getText().toString());
                    //Add to json array
                    jsonArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        //If no links found return null
        if (jsonArray.length() == 0) {
            return null;
        }

        return jsonArray;
    }

}
