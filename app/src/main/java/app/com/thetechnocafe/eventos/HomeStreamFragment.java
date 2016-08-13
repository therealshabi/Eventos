package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gurleensethi on 13/08/16.
 */
public class HomeStreamFragment extends Fragment {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList1;
    private ListView mDrawerList2;
    private String mNavigationList1[];
    private String mNavigationList2[];

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
        mDrawerList1 = (ListView) view.findViewById(R.id.layout_drawer_list_view_1);
        mDrawerList2 = (ListView) view.findViewById(R.id.layout_drawer_list_view_2);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_home_stream_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        //Set up drawer layout
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_icon);
        }
        mNavigationList1 = getResources().getStringArray(R.array.drawer_list_1);
        mNavigationList2 = getResources().getStringArray(R.array.drawer_list_2);
        setUpDrawer();

        return view;
    }

    public List<Data> fill_with_data(List<Data> data) {
        data.add(new Data("GDG Event", "23-08-2016", R.drawable.img));
        data.add(new Data("Knuth Programming", "23-08-2016", R.drawable.img));
        data.add(new Data("JYC Event", "23-08-2016", R.drawable.img));
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView date;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_stream_recycler_view_item_card);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }

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

        public void onAttachedToRecyclerView(RecyclerView recycerView) {
            super.onAttachedToRecyclerView(recycerView);
        }

        public void insert(int position, Data data) {
            list.add(position, data);
            notifyItemInserted(position);
        }

        public void remove(Data data) {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemInserted(position);
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

    private void setUpDrawer() {

        DrawerListAdapter mListAdapter1 = new DrawerListAdapter(getContext(), R.layout.layout_drawer_item, mNavigationList1);
        DrawerListAdapter mListAdapter2 = new DrawerListAdapter(getContext(), R.layout.layout_drawer_item, mNavigationList2);

        mDrawerList1.setAdapter(mListAdapter1);
        mDrawerList2.setAdapter(mListAdapter2);

    }

    private class DrawerListAdapter extends ArrayAdapter {
        private String[] items;

        public DrawerListAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_drawer_item, parent, false);

            ImageView mImageView = (ImageView) view.findViewById(R.id.layout_drawer_item_image);
            TextView mTextView = (TextView) view.findViewById(R.id.layout_drawer_item_text);

            mTextView.setText(items[position]);
            insertDrawerImage(mImageView, items[position]);

            return view;
        }
    }

    private void insertDrawerImage(ImageView imageView, String name) {

        switch (name) {
            case "Favorites": {
                Picasso.with(getContext())
                        .load(R.drawable.navigation_drawer_favorite)
                        .into(imageView);
                break;
            }
            case "Add your event": {
                Picasso.with(getContext())
                        .load(R.drawable.navigation_drawer_new_event)
                        .into(imageView);
                break;
            }
            case "About": {
                Picasso.with(getContext())
                        .load(R.drawable.navigation_drawer_about)
                        .into(imageView);
                break;
            }
            case "Sign Out": {
                Picasso.with(getContext())
                        .load(R.drawable.navigation_drawer_sign_out)
                        .into(imageView);
                break;
            }
        }

    }
}
