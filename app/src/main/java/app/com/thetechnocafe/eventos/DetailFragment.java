package app.com.thetechnocafe.eventos;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.ContactsModel;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Models.LinksModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String EVENT_ID_TAG = "eventid";
    private static String EVENT_ID;
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

    public static DetailFragment getInstance(String id) {
        //Create bundle
        Bundle args = new Bundle();
        args.putString(EVENT_ID_TAG, id);

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

        //Retrieve id from fragment arguments
        EVENT_ID = getArguments().getString(EVENT_ID_TAG, null);
        mEventsDatabaseHelper = new EventsDatabaseHelper(getContext());
        mEvent = mEventsDatabaseHelper.getEvent(EVENT_ID);

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
    }

    //Add contacts in the contacts container
    private void addAndSetUpContacts() {
        //Get the contact list
        final List<ContactsModel> list = mEventsDatabaseHelper.getContactsList(EVENT_ID);

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
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_recent_item, null);
            TextView textView = (TextView) view.findViewById(R.id.comment_recent_item_comment);
            if (i == 0) {
                textView.setText("Nice event");
            } else if (i == 1) {
                textView.setText("Good");
            }
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
        mDateTextView.setText(DateUtils.getFormattedDate(mEvent.getDate()));
        mTimeTextView.setText(DateUtils.convertToTime(mEvent.getDate()));
        mVenueTextView.setText(mEvent.getVenue());
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
}
