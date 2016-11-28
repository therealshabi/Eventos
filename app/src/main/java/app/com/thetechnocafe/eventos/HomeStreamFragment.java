package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import app.com.thetechnocafe.eventos.DataSync.DataSynchronizer;
import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public class HomeStreamFragment extends Fragment {

    public static final String INTENT_EXTRA_EVENT_ID = "eventidintenetextra";
    private static final String DATABASE_NAME = "eventos_database";
    private static final String EVENT_COLUMN_ID = "id";
    private static final String FAV_EVENTS_TABLE = "FavEvents";
    private static final String SELECTED_RECYCLER_POSITION_TAG = "selectedrecyclerposition";
    public LikeButton mLikeButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<EventsModel> mEventsList;
    private RecyclerAdapter mEventRecyclerAdapter;
    private EventsDatabaseHelper mDatabaseHelper;
    private DataSynchronizer mDataSynchronizer;
    private TextView mNavigationHeaderEmailTextView;
    private int mSelectedRecyclerItemPosition = 0;

    public static HomeStreamFragment getInstance() {
        return new HomeStreamFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_stream, container, false);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.fragment_home_stream_drawer);
        mNavigationView = (NavigationView) view.findViewById(R.id.fragment_home_stream_navigation_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_home_stream_swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_stream_recycler_view);
        mNavigationHeaderEmailTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.header_drawer_logged_in_email_text_view);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_home_stream_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_icon);
        }

        //Set up the navigation view (drawer)
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_about: {
                        Intent intent = new Intent(getContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_add_your_event: {
                        Intent intent = new Intent(getContext(), AddTrackEventActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_favorites: {
                        Intent intent = new Intent(getContext(), FavouriteActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_outside_event: {
                        Intent intent = new Intent(getContext(), OutsideEventActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_sign_out: {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you want to Sign Out?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferencesUtils.setLoginState(getContext(), false);

                                //Clear all data from database
                                mDatabaseHelper.removeAllEventsFromDB();

                                //Send to login screen
                                Intent intent = new Intent(getContext(), SigninActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        //Change login state

                        break;
                    }
                    case R.id.menu_calendar_view: {
                        Intent intent = new Intent(getContext(), CalendarViewActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_edit_account: {
                        Intent intent = new Intent(getContext(), EditAccountActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


        //Set up on swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Send request to synchronize data
                mDataSynchronizer.fetchEventsFromNetwork(getContext());
            }
        });

        //Synchronize database with server
        mDataSynchronizer = new DataSynchronizer() {
            @Override
            public void onDataSynchronized(boolean isSyncSuccessful) {
                if (isSyncSuccessful) {
                    setUpAndNotifyRecyclerView();
                } else {
                    //Notify user on sync failed
                    if (isAdded()) {
                        Snackbar.make(view, getString(R.string.sync_failed), Snackbar.LENGTH_SHORT).show();
                    }
                }
                //Stop the Swipe refresh layout
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };

        //Fetch data and set swipe refresh to refreshing
        mDataSynchronizer.fetchEventsFromNetwork(getContext());
        mSwipeRefreshLayout.setRefreshing(true);

        //Assign the database helper
        mDatabaseHelper = new EventsDatabaseHelper(getContext());

        //Set the username
        mNavigationHeaderEmailTextView.setText(SharedPreferencesUtils.getUsername(getContext()));

        //Set up recycler view
        setUpAndNotifyRecyclerView();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.home_stream_menu_refresh: {
                mDataSynchronizer.fetchEventsFromNetwork(getContext());
                mSwipeRefreshLayout.setRefreshing(true);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_stream_menu, menu);
    }

    /**
     * Set up the main recycler view
     * Get events from sqlite database
     * handle the changes after new data is fetched
     */
    private void setUpAndNotifyRecyclerView() {
        //if (mEventRecyclerAdapter == null) {
        //Get the events list from database
        mEventsList = mDatabaseHelper.getEventsList();

        //Create the adapter
        mEventRecyclerAdapter = new RecyclerAdapter(mEventsList, getContext());

        //Set the recycler view with adapter and layout manager
        mRecyclerView.setAdapter(mEventRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     /*   } else {
            //If new data is loaded
            mEventRecyclerAdapter.setUpdatedList(mDatabaseHelper.getEventsList());
            mEventRecyclerAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventsList = mDatabaseHelper.getEventsList();
        for (int count = 0; count < mEventsList.size(); count++) {
            if (mEventRecyclerAdapter != null) {
                mEventRecyclerAdapter.notifyItemChanged(count);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView mCardView;
        private TextView mTitleText;
        private TextView mDateText;
        private ImageView mImageView;
        private EventsModel mEvent;
        private int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCardView = (CardView) itemView.findViewById(R.id.home_stream_recycler_view_item_card);
            mLikeButton = (LikeButton) itemView.findViewById(R.id.home_stream_recycler_view_like_button);
            mTitleText = (TextView) itemView.findViewById(R.id.home_stream_recycler_view_item_title_text);
            mDateText = (TextView) itemView.findViewById(R.id.home_stream_recycler_view_item_date_text);
            mImageView = (ImageView) itemView.findViewById(R.id.home_stream_recycler_view_item_image);
        }

        @Override
        public void onClick(View v) {
            mSelectedRecyclerItemPosition = mPosition;
            Intent intent = new Intent(getContext(), DetailActivity.class);

            //Add event id to intent
            intent.putExtra(INTENT_EXTRA_EVENT_ID, mEvent.getId());

            //Make Shared element transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair<View, String> p1 = Pair.create((View) mTitleText, getString(R.string.shared_title));
                Pair<View, String> p2 = Pair.create((View) mDateText, getString(R.string.shared_date));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
                startActivity(intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        }

        void bindData(int position, final EventsModel event) {
            mEvent = event;
            //Set appropriate data
            mDateText.setText(DateUtils.getFormattedDate(event.getDate()));
            mTitleText.setText(event.getTitle());
            Picasso.with(getContext())
                    .load(mEvent.getImage())
                    .into(mImageView);

            /*
            To check whether if event is in favourite list then like button should be setLiked else not
             */
            String checkId = event.getId();
            if (mDatabaseHelper.doesFavEventAlreadyExists(checkId)) {
                mLikeButton.setLiked(Boolean.TRUE);
            } else {
                mLikeButton.setLiked(Boolean.FALSE);
            }
            mLikeButton.setOnLikeListener(new OnLikeListener() {
                String id = event.getId();
                EventsModel favEventModel = mDatabaseHelper.getEvent(id);

                @Override
                public void liked(LikeButton likeButton) {

                    if (!mDatabaseHelper.doesFavEventAlreadyExists(id)) {
                        mDatabaseHelper.insertNewFavEvent(favEventModel);
                        Toast.makeText(getContext(), event.getTitle() + " added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (mDatabaseHelper.doesFavEventAlreadyExists(id)) {
                        mDatabaseHelper.deleteFavEvent(favEventModel);
                        Toast.makeText(getContext(), event.getTitle() + " removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Adapter for main stream recycler view
    public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<EventsModel> list = Collections.emptyList();
        Context context;

        public RecyclerAdapter(List<EventsModel> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_stream_recycler_view_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //Pass the appropriate EventsModel object
            holder.bindData(position, list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        //Set the updated events list
        public void setUpdatedList(List<EventsModel> updatedList) {
            list = updatedList;
        }
    }

}
