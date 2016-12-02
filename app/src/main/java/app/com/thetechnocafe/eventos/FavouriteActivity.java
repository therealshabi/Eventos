package app.com.thetechnocafe.eventos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
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

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;

import static app.com.thetechnocafe.eventos.HomeStreamFragment.INTENT_EXTRA_EVENT_ID;
import static java.security.AccessController.getContext;

public class FavouriteActivity extends AppCompatActivity {

    private static final String FAV_EVENTS_TABLE = "FavEvents";
    private static final String DATABASE_NAME = "eventos_database";
    private static final String EVENTS_TABLE = "events";
    private static final String EVENT_COLUMN_ID = "id";
    private static final String EVENT_COLUMN_TITLE = "title";
    private static final String EVENT_COLUMN_DESCRIPTION = "description";
    private static final String EVENT_COLUMN_DATE = "date";
    private static final String EVENT_COLUMN_VENUE = "venue";
    private static final String EVENT_COLUMN_AVATAR_ID = "avatar_id";
    private static final String EVENT_COLUMN_IMAGE = "image";
    private static final String EVENT_COLUMN_REQUIREMENTS = "requirements";
    public List<Favourite> favouriteList;
    public String tempId;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private TextView noFavFoundText;
    private ImageView noFavFoundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        favouriteList = new ArrayList<>();

        noFavFoundImage = (ImageView) findViewById(R.id.noFavFoundImage);
        noFavFoundText = (TextView) findViewById(R.id.noFavFoundText);

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

        SQLiteDatabase database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        noFavFoundImage.setVisibility(View.GONE);
        noFavFoundText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        //Create new EventsModel list
        List<EventsModel> eventsList = new ArrayList<>();

        //Set up the query
        String sql = "SELECT * FROM " + FAV_EVENTS_TABLE;

        //Run the query and obtain cursor
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0) {
            //Extract the values while looping over cursor
            while (cursor.moveToNext()) {
                EventsModel event = new EventsModel();
                String newSql = "SELECT * FROM " + EVENTS_TABLE + " where " + EVENT_COLUMN_ID + " = \"" + cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ID)) + "\";";
                Cursor curse = database.rawQuery(newSql, null);
                if (curse != null && curse.getCount() > 0) {
                    curse.moveToFirst();
                    event.setId(curse.getString(curse.getColumnIndex(EVENT_COLUMN_ID)));
                    event.setVenue(curse.getString(curse.getColumnIndex(EVENT_COLUMN_VENUE)));
                    event.setDate(new Date(curse.getLong(curse.getColumnIndex(EVENT_COLUMN_DATE))));
                    event.setAvatarId(curse.getString(curse.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
                    event.setImage(curse.getString(curse.getColumnIndex(EVENT_COLUMN_IMAGE)));
                    event.setTitle(curse.getString(curse.getColumnIndex(EVENT_COLUMN_TITLE)));
                    event.setDescription(curse.getString(curse.getColumnIndex(EVENT_COLUMN_DESCRIPTION)));
                    //Add event to list
                    eventsList.add(event);
                    curse.close();
                }
            }
        }


        //Close cursor after use
        cursor.close();


        int i = 0;
        while (i < eventsList.size()) {
            EventsModel temp = eventsList.get(i);
            String id = temp.getId();
            String title = temp.getTitle();
            String Venue = temp.getVenue();

            String time = "" + String.format("%02d", temp.getDate().getHours()) + ":" + String.format("%02d", temp.getDate().getMinutes()) + " hrs";
            DateTime date = new DateTime(temp.getDate());
            DateTime current = new DateTime(new Date());

            String daysLeft = "" + Math.abs(Days.daysBetween(date.toLocalDate(), current.toLocalDate()).getDays()) + " days left";

            String image = temp.getImage();
            String anchorImage = temp.getAvatarId();
            Favourite fav = new Favourite(id, title, daysLeft, Venue, image, time, anchorImage);
            favouriteList.add(fav);
            i++;
        }

        if (eventsList.size() == 0) {
            noFavFoundImage.setVisibility(View.VISIBLE);
            noFavFoundText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


        // Prototype
        /*Favourite a = new Favourite("12abcd","GDG Dev Fest 2k16", "4 Days to go", "CL2", covers[0], "17:45 hrs");
        favouriteList.add(a);*/

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
            SQLiteDatabase database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            temp = favouriteList.get(position);
            pos = position;
            tempId = temp.getId();
            final String tempTitle = favouriteList.get(pos).getTitle();
            favouriteList.remove(position);
            /*Intent in = new Intent(getBaseContext(),HomeStreamActivity.class);
            in.putExtra("likeBtnStatus",Boolean.FALSE);*/
            String sql = "Delete FROM " + FAV_EVENTS_TABLE + " where " + EVENT_COLUMN_ID + " = \"" + tempId + "\";";
            database.execSQL(sql);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar
                    .make(getCurrentFocus(), tempTitle + " removed from Favourites", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noFavFoundImage.setVisibility(View.GONE);
                            noFavFoundText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            EventsDatabaseHelper mDatabaseHelper = new EventsDatabaseHelper(getBaseContext());
                            EventsModel favEventModel = mDatabaseHelper.getEvent(tempId);

                            if (!mDatabaseHelper.doesFavEventAlreadyExists(tempId)) {
                                mDatabaseHelper.insertNewFavEvent(favEventModel);
                            }
                            favouriteList.add(pos, temp);
                            notifyItemInserted(pos);
                            Snackbar snackbar1 = Snackbar.make(getCurrentFocus(), tempTitle + " restored into the Favourites!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();
            if (favouriteList.size() == 0) {
                noFavFoundImage.setVisibility(View.VISIBLE);
                noFavFoundText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
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

            holder.mTitle.setText(favourite.getTitle());
            holder.mDaysLeft.setText(favourite.getDaystogo());
            holder.mVenue.setText(favourite.getLocation());
            holder.mTime.setText(favourite.getTime());
            holder.mFavEvent = favourite;
            int resourceId = getResources().getIdentifier(favourite.getAnchor(), "drawable", getPackageName());
            holder.mAnchorImage.setImageResource(resourceId);
            Picasso.with(getBaseContext())
                    .load(holder.mFavEvent.getThumbnail())
                    .into(holder.thumbnail);
        }

        public int getItemCount() {
            return favouriteList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView mTitle, mDaysLeft, mVenue, mTime;
            public ImageView thumbnail;
            ImageView mAnchorImage;
            private Favourite mFavEvent;

            public MyViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                mTitle = (TextView) view.findViewById(R.id.favourite_recycler_view_item_title_text);
                mDaysLeft = (TextView) view.findViewById(R.id.favourite_recycler_view_item_time_left_text);
                mVenue = (TextView) view.findViewById(R.id.favourite_recycler_view_item_venue_text);
                mTime = (TextView) view.findViewById(R.id.favourite_recycler_view_item_time_text);
                thumbnail = (ImageView) view.findViewById(R.id.favourite_cardView_image);
                mAnchorImage = (ImageView) view.findViewById(R.id.favourite_recycler_view_item_anchor_image);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);

                //Add event id to intent
                intent.putExtra(INTENT_EXTRA_EVENT_ID, mFavEvent.getId());
                startActivity(intent);

            }
        }


    }

    //ItemCallBack

    public class Favourite {
        public String id;
        public String title;
        public String daystogo;
        public String location;
        public String time;
        public String thumbnail;
        public String anchor;

        public Favourite(String id, String title, String daystogo, String location, String thumbnail, String time, String anchor) {
            this.id = id;
            this.title = title;
            this.daystogo = daystogo;
            this.location = location;
            this.thumbnail = thumbnail;
            this.time = time;
            this.anchor = anchor;
        }

        public String getId() {
            return this.id;
        }

        public String getTitle() {
            return title;
        }

        public void setName(String title) {
            this.title = title;
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

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail)

        {
            this.thumbnail = thumbnail;
        }

        public String getAnchor() {
            return anchor;
        }

        public void setAnchor(String anchor) {
            this.anchor = anchor;
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