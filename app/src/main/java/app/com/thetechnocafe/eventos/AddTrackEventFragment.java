package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddTrackEventFragment extends Fragment {

    private FloatingActionButton mAddNewEventActionButton;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        mAddNewEventActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_add_track_event_fab);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_add_track_event_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_add_track_event_swipe_refresh);

        //Set up FAB
        mAddNewEventActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
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

        //Set up recycler view
        EventAdapter adapter = new EventAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        //Set up swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new OrganisedEventDataFetcher().execute();
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

    //Recycler view holder
    class EventViewHolder extends RecyclerView.ViewHolder {
        private ImageView mVerifiedImage;

        EventViewHolder(View view) {
            super(view);
            mVerifiedImage = (ImageView) view.findViewById(R.id.track_event_item_submitted_verified);
        }

        public void bindData(int position) {
            if (position % 2 == 0) {
                mVerifiedImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_verified));
            }
        }
    }

    //Recycler view adapter
    class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.add_track_event_item, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    class OrganisedEventDataFetcher extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.run();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
