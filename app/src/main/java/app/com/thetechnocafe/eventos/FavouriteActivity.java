package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
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

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

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

    public interface ItemTouchHelperAdapter {

        // Called when an item has been dragged far enough to trigger a move. This is called every time

        void onItemMove(int fromPosition, int toPosition);


        //Called when an item has been dismissed by a swipe
        void onItemDismiss(int position);


    }

    public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
        Favourite temp;
        int pos;
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

        @Override
        public void onItemDismiss(int position) {
            temp = favouriteList.get(position);
            pos = position;
            favouriteList.remove(position);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar
                    .make(getCurrentFocus(), "Message is deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            favouriteList.add(pos, temp);
                            notifyItemInserted(pos);
                            Snackbar snackbar1 = Snackbar.make(getCurrentFocus(), "Message is restored!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(favouriteList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(favouriteList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
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

    //ItemCallBack

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

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }


}



