package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    public List<Favourite> favouriteList;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        favouriteList = new ArrayList<>();

        adapter = new FavouriteAdapter(this, favouriteList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.favourite_toolbar);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }


        prepareFavourites();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void prepareFavourites() {
        int[] covers = new int[]{
                R.drawable.concert,
                R.drawable.calendar,
                R.drawable.dj,
                R.drawable.sports,
                R.drawable.concert,
                R.drawable.concert};


        Favourite a = new Favourite("GDG Dev Fest 2k16", "4 Days to go", "CL2", covers[0], "17:45 hrs");
        favouriteList.add(a);


        Favourite b = new Favourite("Programming Hub Knuth Cup 2k16", "5 Days to go", "Auditorium", covers[1], "15:30 hrs");
        favouriteList.add(b);


        Favourite c = new Favourite("Parola Hub Article Writing Competition on Technology", "6 Days to go", "LRC", covers[2], "12:00 hrs");
        favouriteList.add(c);


        Favourite d = new Favourite("Hackathon By GDG", "8 Days to go", "CL4", covers[3], "18:00 hrs");
        favouriteList.add(d);

    }

    public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
        private Context mContext;
        private List<Favourite> favouriteList;


        public FavouriteAdapter(Context mContext, List<Favourite> favouriteList) {
            this.mContext = mContext;
            this.favouriteList = favouriteList;
        }

        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Favourite favourite = favouriteList.get(position);
            holder.title1.setText(favourite.getName());
            holder.title2.setText(favourite.getDaystogo());
            holder.title3.setText(favourite.getLocation());
            holder.title4.setText(favourite.getTime());
            holder.thumbnail.setImageResource(favourite.getThumbnail());
        }

        public int getItemCount() {
            return favouriteList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title1, title2, title3, title4;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                title1 = (TextView) view.findViewById(R.id.favourite_recycler_view_item_title_text);
                title2 = (TextView) view.findViewById(R.id.favourite_recycler_view_item_time_left_text);
                title3 = (TextView) view.findViewById(R.id.favourite_recycler_view_item_venue_text);
                title4 = (TextView) view.findViewById(R.id.favourite_recycler_view_item_time_text);
                thumbnail = (ImageView) view.findViewById(R.id.favourite_cardView_image);
            }
        }


    }

    public class Favourite {
        public String name;
        public String daystogo;
        public String location;
        public String time;
        public int thumbnail;

        public Favourite(String name, String daystogo, String location, int thumbnail, String time) {
            this.name = name;
            this.daystogo = daystogo;
            this.location = location;
            this.thumbnail = thumbnail;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String Name) {
            this.name = name;
        }

        public String getDaystogo() {
            return daystogo;
        }

        public void setDaystogo(String daystogo) {
            this.daystogo = daystogo;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(int thumbnail)

        {
            this.thumbnail = thumbnail;
        }
    }


}



