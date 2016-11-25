package app.com.thetechnocafe.eventos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Dialogs.DialogDatePicker;
import app.com.thetechnocafe.eventos.Dialogs.DialogTimePicker;
import app.com.thetechnocafe.eventos.Dialogs.LoadingDialog;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

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
    private static final int GALLERY_IMAGE_PICK_REQUEST_CODE = 3;
    private static Bitmap mImageToUpload;
    private static String mImageToUploadName;
    private static String mCloudinaryImageURL = "default";

    private TextView mInfoText;
    private ImageButton mAddContactImageButton;
    private LinearLayout mContactsContainer;
    private ImageButton mAddForumLinkImageButton;
    private LinearLayout mLinkContainer;
    private TextView mDateText;
    private TextView mTimeText;
    private TextView mAddCategoryButton;
    private Button mSubmitButton;
    private RequestUtils mRequestUtils;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mRequirementsEditText;
    private EditText mImageEditText;
    private EditText mVenueEditText;
    private LoadingDialog mLoadingDialog;
    private Calendar mCalendar;
    private ImageButton mPhotoUploadImageButton;
    private ImageView mAddCategoryImageButton;

    private EventsDatabaseHelper mDatabaseHelper;


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
        mPhotoUploadImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_image_button);
        mAddCategoryImageButton = (ImageView) view.findViewById(R.id.fragment_add_event_avatar_image);

        //Get calendar
        mCalendar = GregorianCalendar.getInstance();
        mDatabaseHelper = new EventsDatabaseHelper(getContext());

        //Set up date and text
        setDateText(mCalendar);

        //Set up time text
        setTimeText(mCalendar);

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
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                //TODO:Close the dialog box after form is submitted
                //If successful the finish the activity
                if (isSuccessful) {
                    //Show Toast message and finish the activity
                    Toast.makeText(getContext(), getString(R.string.submission_success), Toast.LENGTH_LONG).show();
                    String username = SharedPreferencesUtils.getUsername(getContext());
                 //   mDatabaseHelper.insertNewUserAddedEvent("",username);
                    startActivity(new Intent(getActivity(),HomeStreamFragment.class));
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
                DialogDatePicker dialogDatePicker = DialogDatePicker.getInstance(mCalendar);
                dialogDatePicker.setTargetFragment(AddEventFragment.this, DATE_PICKER_CODE);
                dialogDatePicker.show(getFragmentManager(), DATE_PICKER_TAG);
            }
        });

        //Set up on click listener for time text view (fire up DialogTimePicker)
        mTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTimePicker dialogTimePicker = DialogTimePicker.getInstance(mCalendar);
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

                    if (mImageToUpload != null) {
                        new ImageUploadAsyncTask().execute();
                    } else {
                        JSONObject object = getJSONObjectFromFormData();
                        mRequestUtils.submitForumToServer(getContext(), object);
                    }
                }
            }
        });

        //Set up on click listeners for photo upload
        mPhotoUploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    }
                }
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_IMAGE_PICK_REQUEST_CODE);
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
                DialogDatePicker.getDate(data, mCalendar);
                setDateText(mCalendar);
            } else if (requestCode == TIME_PICKER_CODE && data != null) {
                DialogTimePicker.getTime(data, mCalendar);
                setTimeText(mCalendar);
            } else if (requestCode == GALLERY_IMAGE_PICK_REQUEST_CODE && data != null) {
                Uri selectedImage = data.getData();
                String projection[] = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                mImageToUpload = BitmapFactory.decodeFile(picturePath);
                File file = new File(picturePath);
                mImageToUploadName = file.getName();
                mImageEditText.setText(mImageToUploadName);
            }
        }
    }

    //Function to set the date text
    private void setDateText(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateText = simpleDateFormat.format(calendar.getTime());
        mDateText.setText(dateText);
    }

    //Function to set time text
    private void setTimeText(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String timeText = simpleDateFormat.format(calendar.getTime());
        mTimeText.setText(timeText);
    }

    //Validate forum before submission
    private boolean validateForum() {
        String errorString = "";
        boolean isValid = true;

        //Validate form
        if (mTitleEditText.getText().toString().length() < 5) {
            //Check title
            mTitleEditText.requestFocus();
            errorString = getString(R.string.form_error_title);
            isValid = false;
        } else if (mDescriptionEditText.getText().toString().length() < 30) {
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
            object.put(StringUtils.SUBMITTED_BY, SharedPreferencesUtils.getUsername(getContext()));
            object.put(StringUtils.JSON_EVENT_DATE, getDateInLong());

            //Get links list
            JSONArray links = getLinksList();
            object.put(StringUtils.JSON_LINKS, links);

            //Get contact list
            JSONArray contacts = getContactsList();
            object.put(StringUtils.JSON_CONTACTS, contacts);
        } catch (JSONException e) {
            mRequestUtils.isRequestSuccessful(false, null);
        }

        return object;
    }

    private JSONArray getLinksList() {
        JSONArray jsonArray = new JSONArray();

        //Traverse the child's
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

        return jsonArray;
    }

    private JSONArray getContactsList() {
        JSONArray jsonArray = new JSONArray();

        //Traverse the childer
        for (int count = 0; count < mContactsContainer.getChildCount(); count++) {
            View view = mContactsContainer.getChildAt(count);

            EditText mNameEditText = (EditText) view.findViewById(R.id.contact_email_phone_name);
            EditText mEmailEditText = (EditText) view.findViewById(R.id.contact_email_phone_email);
            EditText mPhoneEditText = (EditText) view.findViewById(R.id.contact_email_phone_number);

            if (!mNameEditText.getText().toString().equals("") && !mPhoneEditText.getText().toString().equals("")) {
                //Create new json object
                JSONObject object = new JSONObject();
                try {
                    object.put(StringUtils.JSON_CONTACTS_NAME, mNameEditText.getText().toString());
                    object.put(StringUtils.JSON_CONTACTS_EMAIL, mEmailEditText.getText().toString());
                    object.put(StringUtils.JSON_CONTACTS_PHONE, mPhoneEditText.getText().toString());

                    //Add to array
                    jsonArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonArray;
    }

    /**
     * Calculate the date from the two dates
     */
    private long getDateInLong() {
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));

        return calendar.getTimeInMillis();
    }

    class ImageUploadAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", StringUtils.CLOUDINARY_CLOUD_NAME);
            config.put("api_key", StringUtils.CLOUDINARY_API_KEY);
            config.put("api_secret", StringUtils.CLOUDINARY_API_SECRET);
            final Cloudinary cloudinary = new Cloudinary(config);

            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mImageToUpload.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                Map uploadResult = cloudinary.uploader().upload(byteArrayInputStream, ObjectUtils.emptyMap());
                mCloudinaryImageURL = uploadResult.get("url").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mImageEditText.setText(mCloudinaryImageURL);
            JSONObject object = getJSONObjectFromFormData();
            mRequestUtils.submitForumToServer(getContext(), object);
        }
    }
}
