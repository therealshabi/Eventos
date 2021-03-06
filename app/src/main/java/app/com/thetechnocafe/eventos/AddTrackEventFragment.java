package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddTrackEventFragment extends Fragment {

    private static final int GRID_SIZE = 2;
    FloatingActionMenu mFloatingActionMenu;
    private com.github.clans.fab.FloatingActionButton mAddNewEventActionButton;
    private com.github.clans.fab.FloatingActionButton mAddNewOutsideEventActionButton;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestUtils mRequestUtils;
    private List<EventsModel> mSubmittedEventsList;
    private EventAdapter mEventRecyclerAdapter;
    private EventsDatabaseHelper mDatabaseHelper;


    public static AddTrackEventFragment getInstance() {
        return new AddTrackEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_track_event, container, false);

        mAddNewEventActionButton = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu1);
        mAddNewOutsideEventActionButton = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu2);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_add_track_event_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_add_track_event_swipe_refresh);
        mFloatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.fragment_add_track_event_fab);


        //Set up FAB
        mAddNewEventActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        mAddNewOutsideEventActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddOutsideEventActivity.class);
                startActivity(intent);
            }
        });

        //Set up toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_add_track_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        mDatabaseHelper = new EventsDatabaseHelper(getContext());

        setUpAndNotifyRecyclerView();

        mRequestUtils = new RequestUtils() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    setUpAndNotifyRecyclerView();
                } else {
                    //Notify user on sync failed
                    if (isAdded()) {
                    }
                }
                //Stop the Swipe refresh layout
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };

        mRequestUtils.getSubmittedEvents(getContext(), SharedPreferencesUtils.getUsername(getContext()));
        setUpAndNotifyRecyclerView();

        //Set up on swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Send request to synchronize data
                mRequestUtils.getSubmittedEvents(getContext(), SharedPreferencesUtils.getUsername(getContext()));
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

    private void setUpAndNotifyRecyclerView() {
        //if (mEventRecyclerAdapter == null) {
        //Get the events list from database
        mSubmittedEventsList = new ArrayList<>();
        mSubmittedEventsList = mDatabaseHelper.getSubmittedEventsList();


        //Create the adapter
        mEventRecyclerAdapter = new EventAdapter(mSubmittedEventsList, getContext());

        //Set the recycler view with adapter and layout manager
        mRecyclerView.setAdapter(mEventRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     /*   } else {
            //If new data is loaded
            mEventRecyclerAdapter.setUpdatedList(mDatabaseHelper.getEventsList());
            mEventRecyclerAdapter.notifyDataSetChanged();
        }*/
    }

    //Recycler view holder
    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mVerifiedImage;
        TextView mEventTitle;
        TextView mSubmittedOn;
        TextView mRating;
        TextView mNumOfPeopleAttending;
        CardView mCardView;
        TextView mOutsideEventText;
        ImageView mCategoryImage;


        public EventViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mVerifiedImage = (ImageView) view.findViewById(R.id.track_event_item_submitted_verified);
            mEventTitle = (TextView) view.findViewById(R.id.track_event_item_title);
            mSubmittedOn = (TextView) view.findViewById(R.id.track_event_item_submitted_on);
            mRating = (TextView) view.findViewById(R.id.track_event_item_people_rating);
            mNumOfPeopleAttending = (TextView) view.findViewById(R.id.track_event_item_people_attending);
            mCardView = (CardView) view.findViewById(R.id.add_track_event_item_card_view);
            mOutsideEventText = (TextView) view.findViewById(R.id.outsideText);
            mCategoryImage = (ImageView) view.findViewById(R.id.track_event_item_image);
        }

        public void bindData(final EventsModel event) {
            //Toast.makeText(getContext(),"Hello "+event.getSubmittedBy(),Toast.LENGTH_LONG).show();
            mCardView.setVisibility(View.VISIBLE);
            mEventTitle.setText(event.getTitle());
            mSubmittedOn.setText("" + DateUtils.getFormattedDate(new Date()));
            mRating.setText("" + 4);
            mNumOfPeopleAttending.setText("" + event.getNumOfPeopleInterested());

            if (event.getVerified()) {
                mVerifiedImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_verified));
            }

            if (event.getOutsideEvent()) {
                mOutsideEventText.setVisibility(View.VISIBLE);
            } else {
                mOutsideEventText.setVisibility(View.GONE);
            }

            int resourceId = getResources().getIdentifier(event.getAvatarId(), "drawable", getContext().getPackageName());
            mCategoryImage.setImageResource(resourceId);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), TrackEventActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    //Recycler view adapter
    class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        List<EventsModel> list = Collections.emptyList();
        Context context;

        public EventAdapter(List<EventsModel> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.add_track_event_item, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            holder.bindData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
