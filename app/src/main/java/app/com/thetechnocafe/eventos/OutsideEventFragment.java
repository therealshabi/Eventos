package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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

public class OutsideEventFragment extends Fragment {

    RecyclerView recyclerView;
    List<Data> data = new ArrayList<>();
    RecyclerAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_outside_event, container, false);

        data = fill_with_data(data);

        recyclerView = (RecyclerView) view.findViewById(R.id.outside_event_recycler_view);
        adapter = new RecyclerAdapter(data, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_outside_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

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

    public List<Data> fill_with_data(List<Data> data) {
        data.add(new Data("GDG Event", "23-08-2016", R.drawable.calendar));
        data.add(new Data("Knuth Programming", "23-08-2016", R.drawable.calendar));
        data.add(new Data("JYC Event", "23-08-2016", R.drawable.calendar));
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
            mCardView = (CardView) itemView.findViewById(R.id.outside_event_recycler_view_item_card);
            mTitleText = (TextView) itemView.findViewById(R.id.outside_event_recycler_view_item_title_text);
            mDateText = (TextView) itemView.findViewById(R.id.outside_event_recycler_view_item_date_text);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outside_event_recycler_view_item, parent, false);
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
}
