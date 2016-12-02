package app.com.thetechnocafe.eventos;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Dialogs.LoadingDialog;
import app.com.thetechnocafe.eventos.Models.CommentsModel;
import app.com.thetechnocafe.eventos.Models.ContactsModel;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Models.LinksModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

import static app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils.getFullName;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String EVENT_ID_TAG = "eventid";
    private static final String LOADING_DIALOG_TAG = "loading_dialog_tag";
    public static String thisEventId;
    private static String EVENT_ID;
    public RequestUtils mRequestUtils;
    private ViewFlipper mRecentComments;
    private LinearLayout mLinkContainer;
    private LinearLayout mContactsContainer;
    private TextView mShowMoreCommentsText;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mDateTextView;
    private TextView mVenueTextView;
    private TextView mRequirementsTextView;
    private EventsModel mEvent;
    private EventsDatabaseHelper mEventsDatabaseHelper;
    private TextView mNoContactsTextView;
    private TextView mNoLinksTextView;
    private FloatingActionButton mShareFloatingButton;
    private TextView mTimeTextView;
    private ImageButton mSubmitCommentImageButton;
    private EditText mCommentEditText;
    private List<CommentsModel> mCommentsModelsList;
    private ImageView mEventImageView;
    private RatingBar mRatingBar;
    private ToggleButton mInterestedButton;
    private ImageView mAvatarImageView;

    private boolean state = false;

    public static DetailFragment getInstance(String id) {
        //Create bundle
        Bundle args = new Bundle();
        args.putString(EVENT_ID_TAG, id);

        thisEventId = id;

        //Set arguments
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, container, false);

        mTitleTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_title);
        mDescriptionTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_description);
        mDateTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_date);
        mTimeTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_time);
        mVenueTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_venue);
        mRequirementsTextView = (TextView) view.findViewById(R.id.fragment_detail_requirement_text);
        mRecentComments = (ViewFlipper) view.findViewById(R.id.fragment_detail_comment_container);
        mShowMoreCommentsText = (TextView) view.findViewById(R.id.fragment_detail_show_more_comments);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_detail_link_container);
        mContactsContainer = (LinearLayout) view.findViewById(R.id.fragment_detail_contacts_container);
        mNoContactsTextView = (TextView) view.findViewById(R.id.fragment_detail_no_contacts_text);
        mNoLinksTextView = (TextView) view.findViewById(R.id.fragment_detail_forums_text_no_links);
        mShareFloatingButton = (FloatingActionButton) view.findViewById(R.id.fragment_detail_image_share);
        mCommentEditText = (EditText) view.findViewById(R.id.fragment_detail_comment_edit_text);
        mSubmitCommentImageButton = (ImageButton) view.findViewById(R.id.fragment_detail_submit_comment_image_button);
        mEventImageView = (ImageView) view.findViewById(R.id.fragment_detail_event_image_view);
        mRatingBar = (RatingBar) view.findViewById(R.id.fragment_detail_rating_bar);
        mInterestedButton = (ToggleButton) view.findViewById(R.id.interested_radio_group_no);
        mAvatarImageView = (ImageView) view.findViewById(R.id.fragment_detail_image_view);

        //Retrieve id from fragment arguments
        EVENT_ID = getArguments().getString(EVENT_ID_TAG, null);
        mEventsDatabaseHelper = new EventsDatabaseHelper(getContext());
        mEvent = mEventsDatabaseHelper.getEvent(thisEventId);


        //mRatingBar.setRating(0);
        mInterestedButton.setChecked(false);
        //Add comments
        addRecentComments();

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        mRequestUtils = new RequestUtils() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful)
                Log.d("Status", message);
                else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Update the UI
        updateUI();
        setOnClickListeners();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().supportFinishAfterTransition();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //Set onClick listeners for various views
    private void setOnClickListeners() {
        //Set up show more comments
        mShowMoreCommentsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentsActivity.class);
                intent.putExtra(CommentsActivity.EVENT_ID_TAG, EVENT_ID);
                startActivity(intent);
            }
        });

        mShareFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! Attend, " + mEvent.getTitle() + " on " + mEvent.getDate() + " at " + mEvent.getVenue());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        mSubmitCommentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCommentValid()) {
                    submitComment();
                }
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                submitRating((int) rating);
                SharedPreferencesUtils.setRating(getContext(), thisEventId, (int) rating);
            }
        });

        mInterestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterestedButton.isChecked()) {
                    mRequestUtils.increaseDecreaseParticipationEvent(getContext(), thisEventId, 1);
                    SharedPreferencesUtils.setSharedPreferencesToggleState(getContext(), thisEventId, true);
                } else {
                    mRequestUtils.increaseDecreaseParticipationEvent(getContext(), thisEventId, -1);
                    SharedPreferencesUtils.setSharedPreferencesToggleState(getContext(), thisEventId, false);

                }
            }
        });

    }

    //Add contacts in the contacts container
    private void addAndSetUpContacts() {
        //Get the contact list
        final List<ContactsModel> list = mEventsDatabaseHelper.getContactsList(thisEventId);

        //If contacts exist, remove the text view that says "No contacts for this event"
        if (list.size() > 0) {
            mNoContactsTextView.setVisibility(View.GONE);

            //Loop over the list and add them to the container
            for (int i = 0; i < list.size(); i++) {
                final ContactsModel contact = list.get(i);

                View contactView = LayoutInflater.from(getContext()).inflate(R.layout.contacts_item, mContactsContainer, false);
                //Set the data
                TextView mNameText = (TextView) contactView.findViewById(R.id.contacts_item_name);
                TextView mEmailText = (TextView) contactView.findViewById(R.id.contacts_item_email);
                TextView mPhoneText = (TextView) contactView.findViewById(R.id.contacts_item_phone);

                //Set on click listeners for phone and email
                mPhoneText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Send call intent when clicked on phone number
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhoneNumber()));
                        startActivity(callIntent);
                    }
                });

                mEmailText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Send email intent when clicked on email
                        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + contact.getEmailID()));
                        startActivity(Intent.createChooser(intent, getString(R.string.email_chooser)));
                    }
                });


                mNameText.setText(contact.getContactName());
                mPhoneText.setText(contact.getPhoneNumber());
                //If email field does't exists then hide the email view in layout
                if (!list.get(i).getEmailID().equals("")) {
                    mEmailText.setText(contact.getEmailID());
                } else {
                    mEmailText.setVisibility(View.GONE);
                }

                //Add the view to container
                mContactsContainer.addView(contactView);
            }
        }

    }

    private void addAndSetUpLinks() {
        //Get the links list
        List<LinksModel> mList = mEventsDatabaseHelper.getLinksList(EVENT_ID);

        //If links exits remove the no links text view and add all links
        if (mList.size() > 0) {
            mNoLinksTextView.setVisibility(View.GONE);

            for (int i = 0; i < mList.size(); i++) {
                final LinksModel linksModel = mList.get(i);

                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.link_item, mLinkContainer, false);
                textView.setText(linksModel.getLinkName());

                //Set on click listener
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra(WebViewActivity.EXTRA_MESSAGE, linksModel.getLinkAddress());
                        startActivity(intent);
                    }
                });

                //Add to container
                mLinkContainer.addView(textView);
            }
        }
    }

    private void addRecentComments() {
        mCommentsModelsList = mEventsDatabaseHelper.getCommentsList(EVENT_ID);

        //Remove the current items in view flipper
        mRecentComments.removeAllViews();

        int countSize = mCommentsModelsList.size() > 3 ? 3 : mCommentsModelsList.size();

        //Check if no comments
        if (countSize == 0) {
            mShowMoreCommentsText.setText(R.string.no_comments);
            mShowMoreCommentsText.setEnabled(false);
        }

        for (int i = 0; i < countSize; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_recent_item, null);
            TextView commentTextView = (TextView) view.findViewById(R.id.comment_recent_item_comment);
            TextView timeTextView = (TextView) view.findViewById(R.id.comment_recent_item_date);
            TextView fromTextView = (TextView) view.findViewById(R.id.comment_recent_item_id);

            commentTextView.setText(mCommentsModelsList.get(i).getComment());
            timeTextView.setText(DateUtils.getFormattedDate(new Date(mCommentsModelsList.get(i).getTime())));
            fromTextView.setText(mCommentsModelsList.get(i).getFrom());
            mRecentComments.addView(view, i);
        }

        //Change animation
        mRecentComments.setOutAnimation(getContext(), R.anim.slide_out_left);
        mRecentComments.setInAnimation(getContext(), R.anim.slide_in_right);

        //Start the view flipper
        mRecentComments.startFlipping();
    }

    //Update the UI with data
    private void updateUI() {

        //If event is null close the activity
        if (mEvent == null) {
            getActivity().finish();
            return;
        }

        mTitleTextView.setText(mEvent.getTitle());
        mDescriptionTextView.setText(mEvent.getDescription());
        if (mEvent.getDate() != null) {
            mDateTextView.setText(DateUtils.getFormattedDate(mEvent.getDate()));
            mTimeTextView.setText(DateUtils.convertToTime(mEvent.getDate()));
        }
        mVenueTextView.setText(mEvent.getVenue());
        mRatingBar.setRating((float) SharedPreferencesUtils.getRating(getContext(), thisEventId));
        state = SharedPreferencesUtils.getSharedPreferencesToggleState(getContext(), thisEventId);
        int resourceId = getResources().getIdentifier(mEvent.getAvatarId(), "drawable", getContext().getPackageName());
        mAvatarImageView.setImageResource(resourceId);

        if (state) {
            mInterestedButton.setChecked(true);
        } else {
            mInterestedButton.setChecked(false);
        }

        Picasso.with(getContext())
                .load(mEvent.getImage())
                .resize(0, 960)
                .into(mEventImageView);

        //Check if requirements exits
        if (mEvent.getRequirements() != null && !mEvent.getRequirements().isEmpty()) {
            mRequirementsTextView.setText(mEvent.getRequirements());
        }

        //Add the corresponding data
        addAndSetUpLinks();
        addAndSetUpContacts();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Close database
        mEventsDatabaseHelper.close();
    }

    /**
     * Check if comment is valid
     */
    private boolean isCommentValid() {
        if (mCommentEditText.getText().toString().equals("")) {
            mCommentEditText.requestFocus();
            mCommentEditText.setError(getString(R.string.comment_empty));
            return false;
        }
        return true;
    }

    /**
     * Convert the comment details to JSONObject
     */
    private JSONObject getCommentDetails() {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_COMMENT, mCommentEditText.getText().toString());
            object.put(StringUtils.JSON_TIME, new Date().getTime());
            object.put(StringUtils.JSON_FROM, getFullName(getContext()));
            object.put(StringUtils.JSON_EVENT_ID, EVENT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private JSONObject getRatingJSON(int rating) {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_EVENT_ID, thisEventId);
            object.put(StringUtils.JSON_RATING, rating);
            object.put(StringUtils.JSON_USER_ID, SharedPreferencesUtils.getUsername(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * Submit comment to server
     */
    private void submitComment() {

        //Start progress dialog
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(getString(R.string.submitting_comment));
        loadingDialog.show(getFragmentManager(), LOADING_DIALOG_TAG);

        new RequestUtils() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                //Stop the dialog
                loadingDialog.dismiss();
                mEventsDatabaseHelper = new EventsDatabaseHelper(getContext());

                //Check for result
                if (isSuccessful) {
                    Toast.makeText(getContext(), R.string.comment_added_success, Toast.LENGTH_SHORT).show();

                    //Insert into local database
                    CommentsModel commentsModel = new CommentsModel();
                    commentsModel.setEventID(EVENT_ID);
                    commentsModel.setComment(mCommentEditText.getText().toString());
                    commentsModel.setFrom(SharedPreferencesUtils.getFullName(getContext()));
                    commentsModel.setTime(new Date().getTime());
                    mEventsDatabaseHelper.insertNewComment(commentsModel);

                    mCommentEditText.setText("");
                } else {
                    Toast.makeText(getContext(), R.string.comment_add_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }.submitCommentForEvent(getContext(), getCommentDetails());
    }

    private void submitRating(int rating) {
        new RequestUtils() {

            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    Toast.makeText(getContext(), "Event Rated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed Rating Event", Toast.LENGTH_SHORT).show();
                }
            }
        }.setRateEventRequestPost(getContext(), getRatingJSON(rating));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
