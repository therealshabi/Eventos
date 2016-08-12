package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

    RecyclerView recyclerView;
    List<Data> data = new ArrayList<>();
    RecyclerAdapter adapter;

    public static HomeStreamFragment getInstance() {
        return new HomeStreamFragment();
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

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_home_stream_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

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
            cv=(CardView)itemView.findViewById(R.id.home_stream_recycler_view_item_card);
            title=(TextView)itemView.findViewById(R.id.title);

        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<Data> list = Collections.emptyList();
        Context context;

        public RecyclerAdapter(List<Data> list, Context context) {
            this.list=list;
            this.context=context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_stream_recycler_view_item,parent,false);
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

        public void onAttachedToRecyclerView(RecyclerView recycerView)
        {
            super.onAttachedToRecyclerView(recycerView);
        }

        public void insert(int position, Data data)
        {
            list.add(position,data);
            notifyItemInserted(position);
        }

        public void remove(Data data)
        {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemInserted(position);
        }
    }

}
