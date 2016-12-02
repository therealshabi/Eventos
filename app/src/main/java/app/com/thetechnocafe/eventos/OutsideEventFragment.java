package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.thetechnocafe.eventos.DataSync.DataSynchronizer;
import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;

import static java.util.Collections.emptyList;

public class OutsideEventFragment extends Fragment {

    public static final String INTENT_EXTRA_EVENT_ID = "outsideeventidintenetextra";
    RecyclerView recyclerView;
    List<EventsModel> data = new ArrayList<>();
    RecyclerAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private DataSynchronizer mDataSynchronizer;
    private EventsDatabaseHelper mDatabaseHelper;
    private RecyclerAdapter mRecyclerAdapter;
    private List<EventsModel> mEventsList;

    public static OutsideEventFragment getInstance() {
        return new OutsideEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_outside_event, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.outside_event_recycler_view);
        adapter = new RecyclerAdapter(data, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_outside_event_swipe_refresh);


        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_outside_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Send request to synchronize data
                mDataSynchronizer.fetchOutsideEventsFromNetwork(getContext());
            }
        });

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

        mDataSynchronizer.fetchOutsideEventsFromNetwork(getContext());
        mSwipeRefreshLayout.setRefreshing(true);

        //Assign the database helper
        mDatabaseHelper = new EventsDatabaseHelper(getContext());

        setUpAndNotifyRecyclerView();

        return view;
    }

    private void setUpAndNotifyRecyclerView() {

        mEventsList = mDatabaseHelper.getOutsideEventsList();

        //Create the adapter
        mRecyclerAdapter = new RecyclerAdapter(mEventsList, getContext());

        //Set the recycler view with adapter and layout manager
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    @Override
    public void onResume() {
        super.onResume();
        mEventsList = mDatabaseHelper.getOutsideEventsList();

        for (int count = 0; count < mEventsList.size(); count++) {
            if (mRecyclerAdapter != null) {
                mRecyclerAdapter.notifyItemChanged(count);
            }
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        private TextView mTitleText;
        private TextView mDateText;
        private ImageView mImageView;
        private EventsModel mEvent;
        private TextView mTimeText;
        private ImageView mCategoryImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCardView = (CardView) itemView.findViewById(R.id.outside_event_recycler_view_item_card);
            mTitleText = (TextView) itemView.findViewById(R.id.outside_event_recycler_view_item_title_text);
            mDateText = (TextView) itemView.findViewById(R.id.outside_event_recycler_view_item_date_text);
            mImageView = (ImageView) itemView.findViewById(R.id.outside_event_recycler_view_item_image);
            mTimeText = (TextView) itemView.findViewById(R.id.outside_event_recycler_view_item_time_text);
            mCategoryImage = (ImageView) itemView.findViewById(R.id.outside_event_recycler_view_item_anchor_image);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), DetailOutsideEventActivity.class);

            //Add event id to intent
            intent.putExtra(INTENT_EXTRA_EVENT_ID, mEvent.getId());

            //Make Shared element transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair<View, String> p1 = Pair.create((View) mTitleText, getString(R.string.shared_title));
                Pair<View, String> p2 = Pair.create((View) mDateText, getString(R.string.shared_date));
                Pair<View, String> p3 = Pair.create((View) mCategoryImage, getString(R.string.shared_avatar));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
                startActivity(intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        }

        void bindData(int position, final EventsModel event) {
            mEvent = event;
            //Set appropriate data
            mDateText.setText(DateUtils.getFormattedDate(event.getDate()));
            mTimeText.setText(DateUtils.convertToTime(event.getDate()));
            mTitleText.setText(event.getTitle());
            Picasso.with(getContext())
                    .load(mEvent.getImage())
                    .resize(0, 800)
                    .into(mImageView);

            int resourceId = getResources().getIdentifier(event.getAvatarId(), "drawable", getContext().getPackageName());
            mCategoryImage.setImageResource(resourceId);
        }
    }

    //Adapter
    public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<EventsModel> list = emptyList();
        Context context;

        public RecyclerAdapter(List<EventsModel> list, Context context) {
            this.list = list;
            this.context = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outside_event_recycler_view_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindData(position, this.list.get(position));
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }
}
