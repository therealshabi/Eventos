package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gurleensethi on 13/08/16.
 */
public class HomeStreamFragment extends Fragment {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView recyclerView;
    List<Data> data = new ArrayList<>();
    RecyclerAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_home_stream, container, false);

        data = fill_with_data(data);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_stream_recycler_view);
        adapter = new RecyclerAdapter(data, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.fragment_home_stream_drawer);
        mNavigationView = (NavigationView) view.findViewById(R.id.fragment_home_stream_navigation_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_home_stream_swipe_refresh);

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
                        return true;
                    }
                    case R.id.menu_add_your_event: {
                        Intent intent = new Intent(getContext(), AddTrackEventActivity.class);
                        startActivity(intent);
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
                new DataFetcher().execute();
            }
        });

        return view;
    }

    public List<Data> fill_with_data(List<Data> data) {
        data.add(new Data("GDG Event", "23-08-2016", R.drawable.img));
        data.add(new Data("Knuth Programming", "23-08-2016", R.drawable.img));
        data.add(new Data("JYC Event", "23-08-2016", R.drawable.img));
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardView;
        private TextView mTitleText;
        private TextView mDateText;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCardView = (CardView) itemView.findViewById(R.id.home_stream_recycler_view_item_card);
            mTitleText = (TextView) itemView.findViewById(R.id.home_stream_recycler_view_item_title_text);
            mDateText = (TextView) itemView.findViewById(R.id.home_stream_recycler_view_item_date_text);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair<View, String> p1 = Pair.create((View) mTitleText, getString(R.string.shared_title));
                Pair<View, String> p2 = Pair.create((View) mDateText, getString(R.string.shared_date));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
                startActivity(intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    //Adapter
    public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<Data> list = Collections.emptyList();
        Context context;

        public RecyclerAdapter(List<Data> list, Context context) {
            this.list = list;
            this.context = context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_stream_recycler_view_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class DataFetcher extends AsyncTask<Void, Void, Void> {

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
