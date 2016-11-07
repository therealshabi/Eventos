package app.com.thetechnocafe.eventos;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private LinearLayout mRecentComments;
    private TextView mShowMoreCommentsText;
    private LinearLayout mLinkContainer;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mDateTextView;
    private TextView mVenueTextView;
    private TextView mRequirementsTextView;
    private static final String EVENT_ID_TAG = "eventid";
    private static String EVENT_ID;
    private EventsModel mEvent;
    private EventsDatabaseHelper mEventsDatabaseHelper;

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
        mVenueTextView = (TextView) view.findViewById(R.id.fragment_detail_text_view_venue);
        mRequirementsTextView = (TextView) view.findViewById(R.id.fragment_detail_requirement_text);
        mRecentComments = (LinearLayout) view.findViewById(R.id.fragment_detail_comment_container);
        mShowMoreCommentsText = (TextView) view.findViewById(R.id.fragment_detail_show_more_comments);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_detail_link_container);

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
        addLink();

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

    private void addRecentComments() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_recent_item, null);
            TextView textView = (TextView) view.findViewById(R.id.comment_recent_item_comment);
            if (i == 0) {
                textView.setText("Nice event");
            } else if (i == 1) {
                textView.setText("Good");
            }
            mRecentComments.addView(view);
        }
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
    }

    private void addLink() {
        for (int i = 0; i < 1; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.link_item, null);
            TextView textView = (TextView) view.findViewById(R.id.link_item_text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.EXTRA_MESSAGE, "http://www.google.com");
                    startActivity(intent);
                }
            });
            mLinkContainer.addView(view);
        }
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
        mDateTextView.setText(mEvent.getDate().toString());
        mVenueTextView.setText(mEvent.getVenue());
        //Check if requirements exits
        if (mEvent.getRequirements() != null && !mEvent.getRequirements().isEmpty()) {
            mRequirementsTextView.setText(mEvent.getRequirements());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mEventsDatabaseHelper.close();
    }
}
